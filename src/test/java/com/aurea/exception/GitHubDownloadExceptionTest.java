package com.aurea.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

public class GitHubDownloadExceptionTest {

  private final String downloadFailedMessage = "Download Failed";

  @Test
  public void messageTest(){
    assertThatThrownBy(()-> {throw new GitHubDownloadException(downloadFailedMessage);})
    .hasMessage(downloadFailedMessage);
  }
}