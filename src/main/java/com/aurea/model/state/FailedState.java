package com.aurea.model.state;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;

public class FailedState implements StateConsumer {

  private String errorMessage;

  public FailedState(String message){
    errorMessage = message;
  }

  @Override
  public void accept(DeadCodeDetection deadCodeDetection) {
    deadCodeDetection.setDeadCodeDetectionStatus(DeadCodeDetectionStatus.FAILED);
    deadCodeDetection.setErrorMessage(errorMessage);
  }

}
