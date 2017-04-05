package com.aurea.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

public class GitHubDownloadExceptionTest {

  private final String failMessage = "Download Failed";

  @Test
  public void messageTest() {
    assertThatThrownBy(() -> {
      throw new GitHubDownloadException(failMessage);
    }).isInstanceOf(RuntimeException.class)
        .hasMessage(failMessage);
  }
}