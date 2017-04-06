package com.aurea.service;

import com.aurea.exception.GitHubDownloadException;
import com.aurea.model.DeadCodeDetection;
import com.aurea.model.GitHubRepoUrl;
import com.aurea.model.state.DownloadingRepoState;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GitService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MethodHandles.lookup().lookupClass());

  void downloadRepo(DeadCodeDetection deadCodeDetection, File localDir) {

    LOGGER.info("Downloading repository for :{}", deadCodeDetection.getId());
    deadCodeDetection.setState(new DownloadingRepoState());

    try {
      cloneRepo(deadCodeDetection, localDir);
    } catch (GitAPIException | IOException e) {
      throw new GitHubDownloadException(e.getMessage());
    }
  }

  void cloneRepo(DeadCodeDetection deadCodeDetection, File localDir)
      throws GitAPIException, IOException {

    GitHubRepoUrl gitHubRepoUrl = deadCodeDetection.getGitHubRepoUrl();
    String repoUrl = gitHubRepoUrl.getRepoUrl().toString();

    Git git = Git.cloneRepository()
        .setDirectory(localDir)
        .setURI(repoUrl)
        .call();

    git.getRepository().close();
  }

}
