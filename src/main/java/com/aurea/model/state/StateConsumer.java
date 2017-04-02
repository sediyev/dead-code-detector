package com.aurea.model.state;

import com.aurea.model.DeadCodeDetection;

public interface StateConsumer {
  public void accept(DeadCodeDetection deadCodeDetection);
}
