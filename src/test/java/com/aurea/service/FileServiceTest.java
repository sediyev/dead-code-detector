package com.aurea.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileServiceTest {

  private final FileService fileService = new FileService();

  private final String udbFileName = "database.udb";

  @Test
  public void getUdbPath() {
    File rootFile = Files.createTempDir();
    String expectedUdbPath = rootFile.getAbsolutePath() + udbFileName;

    assertThat(fileService.getUdbPath(rootFile)).isEqualTo(expectedUdbPath);
  }

  @Test
  public void tryToCleanDirectory() throws IOException {
    File rootFile = Files.createTempDir();

    File tempFile = new File(rootFile, "temp.txt");
    FileUtils.writeStringToFile(tempFile, "testData");

    assertThat(tempFile).exists().isFile();

    fileService.tryToCleanRootDirectory(rootFile);

    assertThat(tempFile).doesNotExist();
  }

  @Test
  public void cleaningNullDirectoryDoesNotFail() {
    fileService.tryToCleanRootDirectory(null);
  }

}