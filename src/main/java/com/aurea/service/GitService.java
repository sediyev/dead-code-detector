package com.aurea.service;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

@Service
public class GitService {

  public void cloneRepo(String repoUrl, File localDir, String branch) throws GitAPIException {

    Git git = Git.cloneRepository()
        .setBranch(branch)
        .setDirectory(localDir)
        .setURI(repoUrl)
        .call();

    git.getRepository().close();
  }

}
