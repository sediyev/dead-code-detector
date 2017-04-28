package com.aurea.service;

import com.aurea.exception.GitHubDownloadException;
import com.aurea.model.DeadCodeDetection;
import com.aurea.model.GitHubRepo;
import com.aurea.model.state.DownloadingRepoState;
import com.aurea.model.state.FailedState;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.apache.commons.lang3.StringUtils;
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
      deadCodeDetection.setState(new FailedState("Error downloading git project!"));
      LOGGER.error("Error processing deadCodeDetection: {}", deadCodeDetection.getId());
      throw new GitHubDownloadException(e.getMessage());
    }
  }

  void cloneRepo(DeadCodeDetection deadCodeDetection, File localDir)
      throws GitAPIException, IOException {

    GitHubRepo gitHubRepo = deadCodeDetection.getGitHubRepo();
    String repoUrl = gitHubRepo.getRepoUrl().toString();
    String branch = gitHubRepo.getBranch();

    Git git = Git.cloneRepository()
        .setDirectory(localDir)
        .setBranch(StringUtils.isBlank(branch) ? null : branch)
        .setURI(repoUrl)
        .call();

    git.getRepository().close();

    //TODO change GitHubDownloadException to BranchNotFoundException
    if (localDir.list() != null && localDir.list().length <= 1) {
      deadCodeDetection.setState(new FailedState("Branch not found!"));
      throw new GitHubDownloadException("Branch not found!" + branch);
    }

  }

}
