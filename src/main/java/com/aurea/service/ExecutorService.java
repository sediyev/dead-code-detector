package com.aurea.service;

import com.aurea.exception.GitHubDownloadException;
import com.aurea.model.DeadCodeDetection;
import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.model.state.CompletedState;
import com.aurea.model.state.DownloadingRepoState;
import com.aurea.model.state.FailedState;
import com.aurea.model.state.ProcessingState;
import com.google.common.io.Files;
import com.scitools.understand.Database;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ExecutorService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  private final GitService gitService;
  private final FileService fileService;
  private final UnderstandService understandService;

  private final java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();

//  private static final Database = Understand.open();

  public ExecutorService(GitService gitService, FileService fileService,
      UnderstandService understandService) {
    this.gitService = gitService;
    this.fileService = fileService;
    this.understandService = understandService;
  }

  @Async
  public void executeDeadCodeDetection(DeadCodeDetection deadCodeDetection) {

    LOGGER.info("Started async processing of: {}", deadCodeDetection.getId());

    executor.submit(() -> {
      File rootFile = null;
      Database database = null;
      try {
        rootFile = Files.createTempDir();
        downloadRepo(deadCodeDetection, rootFile);
        deadCodeDetection.setState(new ProcessingState());

        String udbFilePath = fileService.getUdbPath(rootFile);
        // Create udb database
        understandService.createUdb(udbFilePath, rootFile.getAbsolutePath());

        LOGGER.info("Opening udb database");
        database = Understand.open(udbFilePath);
        LOGGER.info("Opened udb database");

        List<UnusedUnderstandEntity> deadCodeList = understandService.findAllDeadCode(database, rootFile.getAbsolutePath());

        LOGGER.info("Finished processing algorithms");

        deadCodeDetection.setDeadCodeList(deadCodeList);
        deadCodeDetection.setState(new CompletedState());
      } catch (InterruptedException | IOException | TimeoutException | UnderstandException e) {
        deadCodeDetection.setState(new FailedState());
        LOGGER.error("Error executing deadCodeDetection with id: " + deadCodeDetection.getId(), e);
      } finally {

        // order of cleanup commands are important. Folder cannot be deleted while udb database is open

        if (database != null) {
          database.close();
        }

        fileService.tryToCleanRootDirectory(rootFile);
      }
    });
  }

  private void downloadRepo(DeadCodeDetection deadCodeDetection, File localDir) {

    deadCodeDetection.setState(new DownloadingRepoState());

    try {
      gitService.cloneRepo(deadCodeDetection, localDir);
    } catch (GitAPIException | IOException e) {
      throw new GitHubDownloadException(e.getMessage());
    }
  }
}
