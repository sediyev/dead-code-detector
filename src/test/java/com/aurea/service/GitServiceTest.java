package com.aurea.service;

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

  GitService gitService = new GitService();

  // TODO give local path for this directory
  private final String localGitRepository = "d:\\delete";
  private final File localDir = new File(localGitRepository);
  private final String repoUrl = "https://github.com/sediyev/dead-code-detector.git";

  @Before
  public void setup() throws IOException {
    System.out.println("Resetting directory: "+localGitRepository);
    FileUtils.cleanDirectory(localDir);
  }

  @After
  public void tearDown() throws IOException {
    System.out.println("Deleting directory");
    FileUtils.cleanDirectory(localDir);
  }

  // As cloning git repository from Github is slow; this test is not intended for automatic tests
  @Ignore("Takes too long")
  @Test
  public void cloneRepositoryTest() throws GitAPIException, IOException {

    System.out.println("Cloning git repository to local directory");
    gitService.cloneRepo(repoUrl,localDir,"master");


  }

}