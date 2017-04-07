package com.aurea.service;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.model.state.CompletedState;
import com.aurea.model.state.FailedState;
import com.aurea.model.state.ProcessingState;
import com.aurea.model.state.WaitingInQueueState;
import com.google.common.io.Files;
import com.scitools.understand.Database;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.InvalidExitValueException;

@Service
public class ExecutorService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final GitService gitService;
  private final FileService fileService;
  private final UnderstandService understandService;

  // Scitools Understand API does not support multi-threading.
  private final java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();

  public ExecutorService(GitService gitService, FileService fileService,
      UnderstandService understandService) {
    this.gitService = gitService;
    this.fileService = fileService;
    this.understandService = understandService;
  }

  Database createAndGetUdbDatabase(File rootFile, DeadCodeDetection deadCodeDetection)
      throws InterruptedException, IOException, TimeoutException, UnderstandException {

    deadCodeDetection.setState(new ProcessingState());
    String udbFilePath = fileService.getUdbPath(rootFile);

    understandService.createUdbDatabase(udbFilePath, rootFile.getAbsolutePath());

    LOGGER.info("Opening udb database: {}", udbFilePath);
    Database database = Understand.open(udbFilePath);
    LOGGER.info("Opened udb database");

    return database;
  }

  void findAndSetDeadCodes(DeadCodeDetection deadCodeDetection,
      Database database, File rootFile) {
    List<UnusedUnderstandEntity> deadCodeList = understandService
        .findAllDeadCode(database, rootFile.getAbsolutePath());

    LOGGER.info("Finished processing algorithms: {}", deadCodeDetection.getId());

    deadCodeDetection.setDeadCodeList(deadCodeList);
    deadCodeDetection.setState(new CompletedState());
  }

  // Non-blocking call to download repo, create understand db and run algorithms
  @Async
  public Future<?> executeDeadCodeDetection(DeadCodeDetection deadCodeDetection) {

    File rootFile = Files.createTempDir();
    gitService.downloadRepo(deadCodeDetection, rootFile);

    return addToQueue(deadCodeDetection, rootFile);
  }

  Future<?> addToQueue(DeadCodeDetection deadCodeDetection, File rootFile) {
    deadCodeDetection.setState(new WaitingInQueueState());

    return executor.submit(() -> executeDeadCodeDetection(deadCodeDetection, rootFile));
  }

  void executeDeadCodeDetection(DeadCodeDetection deadCodeDetection, File rootFile) {
    LOGGER.info("Started synchronous processing of: {}", deadCodeDetection.getId());

    Database database = null;
    try {
      database = createAndGetUdbDatabase(rootFile, deadCodeDetection);
      findAndSetDeadCodes(deadCodeDetection, database, rootFile);

    } catch (TimeoutException e) {
      deadCodeDetection.setState(new FailedState("Timed out while creating udb database"));
      LOGGER.error("TimeoutException executing deadCodeDetection with id: " + deadCodeDetection.getId(), e);
    } catch (InvalidExitValueException e) {
      deadCodeDetection.setState(new FailedState("Creating udb database failed."));
      LOGGER.error("InvalidExitValueException executing deadCodeDetection with id: " + deadCodeDetection.getId(), e);

    } catch (Exception e) {
      deadCodeDetection.setState(new FailedState(e.getMessage()));
      LOGGER.error("Exception executing deadCodeDetection with id: " + deadCodeDetection.getId(), e);

    } finally {

      // order of cleanup commands are important. Folder cannot be deleted while udb database is open
      if (database != null) {
        database.close();
      }

      fileService.tryToCleanRootDirectory(rootFile);
    }


  }

}
