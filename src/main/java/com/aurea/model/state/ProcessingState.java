package com.aurea.model.state;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;
import java.time.LocalDateTime;

public class ProcessingState implements StateConsumer {
  @Override
  public void accept(DeadCodeDetection deadCodeDetection) {
    deadCodeDetection.setExecutionStartTime(LocalDateTime.now());
    deadCodeDetection.setDeadCodeDetectionStatus(DeadCodeDetectionStatus.PROCESSING);
  }

}
