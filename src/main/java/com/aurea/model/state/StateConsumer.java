package com.aurea.model.state;

import com.aurea.model.DeadCodeDetection;

public interface StateConsumer {
  void accept(DeadCodeDetection deadCodeDetection);
}
