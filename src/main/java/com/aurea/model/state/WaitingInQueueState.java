package com.aurea.model.state;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;

public class WaitingInQueueState implements StateConsumer {

  @Override
  public void accept(DeadCodeDetection deadCodeDetection) {
    deadCodeDetection.setDeadCodeDetectionStatus(DeadCodeDetectionStatus.IN_QUEUE);
  }

}
