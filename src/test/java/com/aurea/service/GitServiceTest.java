package com.aurea.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aurea.exception.GitHubDownloadException;
import com.aurea.model.DeadCodeDetectionStatus;
import com.aurea.util.AbstractDeadCodeDetectionTest;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GitServiceTest extends AbstractDeadCodeDetectionTest{

  private  GitService gitService;

  private File localDir;
  private final String repoUrl = "https://github.com/sediyev/dead-code-detector.git";

  @Before
  public void setup() {
    localDir = Files.createTempDir();
    gitService = Mockito.spy(new GitService());
  }

  @After
  public void tearDown() throws IOException {
    System.out.println("Cleaning temp directory");
    FileUtils.cleanDirectory(localDir);
  }

  @Test
  public void downloadRepoSetsCorrectStatus() throws GitAPIException, IOException {

    Mockito.doNothing().when(gitService).cloneRepo(deadCodeDetection, localDir);

    gitService.downloadRepo(deadCodeDetection, localDir);
    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.DOWNLOADING_REPO);

  }

  @Test
  public void downloadRepoThrowsGitApiException() throws GitAPIException, IOException {

    Mockito.doThrow(IOException.class).when(gitService).cloneRepo(deadCodeDetection, localDir);

    assertThatThrownBy(() ->gitService.downloadRepo(deadCodeDetection, localDir)).isInstanceOf(
        GitHubDownloadException.class);

  }

  // As cloning git repository from Github is slow; this test will take longer to execute
//  @Ignore("Takes too long")
  @Test
  public void cloneRepositoryTest() throws GitAPIException, IOException {

    File localDir = Files.createTempDir();

    System.out.println("Cloning git repository to local directory");

    gitService.cloneRepo(deadCodeDetection, localDir);

    FileUtils.cleanDirectory(localDir);

  }

}