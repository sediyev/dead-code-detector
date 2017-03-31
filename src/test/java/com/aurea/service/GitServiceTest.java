package com.aurea.service;

import java.io.File;
import java.io.IOException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GitServiceTest {

  @Autowired
  GitService gitService;

  private final String localGitRepository = "d:\\delete";
  private final File localDir = new File(localGitRepository);

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

  @Test
  public void cloneRepositoryTest() throws GitAPIException, IOException {

    gitService.cloneRepo("https://github.com/eclipse/jgit.git",localDir,"master");

    System.out.println("Created directory");

  }

}