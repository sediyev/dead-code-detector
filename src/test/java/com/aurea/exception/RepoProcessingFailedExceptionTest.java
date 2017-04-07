package com.aurea.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.ws.rs.WebApplicationException;
import org.junit.Test;

public class RepoProcessingFailedExceptionTest {

  private static final String PROCESSING_FAILED = "Processing Failed";

  @Test
  public void messageTest() {
    assertThatThrownBy(() -> {
      throw new RepoProcessingFailedException(PROCESSING_FAILED);
    }).isInstanceOf(WebApplicationException.class)
        .hasMessage(PROCESSING_FAILED)
        .hasNoCause();
  }
}