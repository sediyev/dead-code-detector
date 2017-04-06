package com.aurea.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.ws.rs.NotFoundException;
import org.junit.Test;

public class RepoNotFoundExceptionTest {

  @Test
  public void messageTest() {
    assertThatThrownBy(() -> {
      throw new RepoNotFoundException();
    }).isInstanceOf(NotFoundException.class)
        .hasNoCause();
  }
}