package com.aurea.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ExecutorService {

  @Async
  public void executeDeadCodeDetection(){
    System.out.println("Invoked" + Thread.currentThread().getName());
  }
}
