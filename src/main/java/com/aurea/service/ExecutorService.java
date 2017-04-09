package com.aurea.service;

import static com.aurea.util.FileUtils.tryToCleanRootDirectory;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.state.FailedState;
import com.aurea.model.state.WaitingInQueueState;
import com.google.common.io.Files;
import com.scitools.understand.Database;
import java.io.File;
import java.lang.invoke.MethodHandles;
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
  private final UnderstandService understandService;

  // Scitools Understand API does not support multi-threading.
  private final java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();

  public ExecutorService(GitService gitService, UnderstandService understandService) {
    this.gitService = gitService;
    this.understandService = understandService;
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
      database = understandService.createAndGetUdbDatabase(rootFile, deadCodeDetection);
      understandService.findAndSetDeadCodes(deadCodeDetection, database, rootFile);

    } catch (TimeoutException e) {
      deadCodeDetection.setState(new FailedState("Timed out while creating udb database"));
      LOGGER.error(
          "TimeoutException executing deadCodeDetection with id: " + deadCodeDetection.getId(), e);
    } catch (InvalidExitValueException e) {
      deadCodeDetection.setState(new FailedState("Creating udb database failed."));
      LOGGER.error(
          "InvalidExitValueException executing deadCodeDetection with id: " + deadCodeDetection
              .getId(), e);

    } catch (Exception e) {
      deadCodeDetection.setState(new FailedState(e.getMessage()));
      LOGGER
          .error("Exception executing deadCodeDetection with id: " + deadCodeDetection.getId(), e);

    } finally {

      // order of cleanup commands are important. Folder cannot be deleted while udb database is open
      if (database != null) {
        database.close();
      }

      tryToCleanRootDirectory(rootFile);
    }


  }

}
