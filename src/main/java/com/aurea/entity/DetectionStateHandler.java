package com.aurea.entity;

import java.time.LocalDateTime;

public class DetectionStateHandler {

  public static void setPending(DetectionDetails detectionDetails){
    detectionDetails.setTimeRepoIsAdded(LocalDateTime.now());
    detectionDetails.setDetectionState(DetectionState.PENDING);
  }

  public static void setInQueue(DetectionDetails detectionDetails){
    detectionDetails.setDetectionState(DetectionState.IN_QUEUE);
  }

  public static void setProcessing(DetectionDetails detectionDetails){
    detectionDetails.setExecutionStartTime(LocalDateTime.now());
    detectionDetails.setDetectionState(DetectionState.PROCESSING);
  }

  public static void setCompleted(DetectionDetails detectionDetails){
    detectionDetails.setExecutionEndTime(LocalDateTime.now());
    detectionDetails.setDetectionState(DetectionState.COMPLETED);
  }

  public static void setFailed(DetectionDetails detectionDetails){
    detectionDetails.setDetectionState(DetectionState.FAILED);
  }

}
