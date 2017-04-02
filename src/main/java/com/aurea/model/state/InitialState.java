package com.aurea.model.state;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;
import java.time.LocalDateTime;

public class InitialState implements StateConsumer{

  @Override
  public void accept(DeadCodeDetection deadCodeDetection) {
    deadCodeDetection.setTimeRepoIsAdded(LocalDateTime.now());
    deadCodeDetection.setDeadCodeDetectionStatus(DeadCodeDetectionStatus.ADDED);
  }
}
