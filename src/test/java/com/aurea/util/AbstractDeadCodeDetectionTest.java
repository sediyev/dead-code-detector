package com.aurea.util;

import com.aurea.model.DeadCodeDetection;

public abstract class AbstractDeadCodeDetectionTest {

  protected DeadCodeDetection deadCodeDetection = createDeadCodeDetection();

  protected final String repoUrl = "https://github.com/sediyev/dead-code-detector.git";
  private static final String BRANCH = "master";

  private DeadCodeDetection createDeadCodeDetection() {
    DeadCodeDetection deadCodeDetection = new DeadCodeDetection(repoUrl, BRANCH);
    deadCodeDetection.setId(1L);

    return deadCodeDetection;
  }
}
