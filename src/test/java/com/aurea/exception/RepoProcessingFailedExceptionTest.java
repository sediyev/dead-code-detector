package com.aurea.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.ws.rs.WebApplicationException;
import org.junit.Test;

public class RepoProcessingFailedExceptionTest {

  @Test
  public void messageTest() {
    assertThatThrownBy(() -> {
      throw new RepoProcessingFailedException();
    }).isInstanceOf(WebApplicationException.class)
        .hasNoCause();
  }
}