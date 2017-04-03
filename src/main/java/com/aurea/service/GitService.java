package com.aurea.service;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.GitHubRepoUrl;
import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

@Service
public class GitService {

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
