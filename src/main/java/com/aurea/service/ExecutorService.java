package com.aurea.service;

import com.aurea.entity.DetectionDetails;
import com.aurea.entity.GitDetails;
import java.io.File;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ExecutorService {

  private final GitService gitService;

  @Value("${local.git.root.repo}")
  private String localFileDirectory;

  public ExecutorService(GitService gitService){
    this.gitService = gitService;
  }

  @Async
  public void executeDeadCodeDetection(DetectionDetails detectionDetails) throws GitAPIException {
    System.out.println("Invoked" + Thread.currentThread().getName());

    GitDetails gitDetails = detectionDetails.getGitDetails();
    gitService.cloneRepo(gitDetails.getRepoUrl(), new File(localFileDirectory), "master");





  }
}
