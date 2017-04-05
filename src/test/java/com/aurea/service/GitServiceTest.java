package com.aurea.service;

import com.aurea.model.DeadCodeDetection;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GitServiceTest {

  private final GitService gitService = new GitService();
  private final DeadCodeDetectionService deadCodeDetectionService = new DeadCodeDetectionService();

  private final File localDir = Files.createTempDir();
  private final String repoUrl = "https://github.com/sediyev/dead-code-detector.git";

  @Before
  public void setup() {
  }

  @After
  public void tearDown() throws IOException {
    System.out.println("Cleaning temp directory");
    FileUtils.cleanDirectory(localDir);
  }

  // As cloning git repository from Github is slow; this test is not intended for automatic tests
  @Ignore("Takes too long")
  @Test
  public void cloneRepositoryTest() throws GitAPIException, IOException {

    File localDir = Files.createTempDir();

    System.out.println("Cloning git repository to local directory");
    DeadCodeDetection deadCode = deadCodeDetectionService.create(repoUrl);
    System.out.println(deadCode.getGitHubRepoUrl().getRepoUrl());

    gitService.cloneRepo(deadCode, localDir);

  }

}