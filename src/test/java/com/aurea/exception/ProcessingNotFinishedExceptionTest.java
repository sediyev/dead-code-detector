package com.aurea.exception;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aurea.model.DeadCodeDetectionStatus;
import org.junit.Test;

public class ProcessingNotFinishedExceptionTest {

  @Test
  public void messageTest() {
    assertThatThrownBy(() -> {
      throw new ProcessingNotFinishedException(DeadCodeDetectionStatus.IN_QUEUE);
    }).isInstanceOf(ProcessingNotFinishedException.class)
        .hasMessageContaining(DeadCodeDetectionStatus.IN_QUEUE.name())
        .hasNoCause();
  }
}