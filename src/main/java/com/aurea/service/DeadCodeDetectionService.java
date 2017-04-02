package com.aurea.service;

import com.aurea.model.DeadCodeDetection;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class DeadCodeDetectionService {

  /**
   * Used to create unique id for each deadCodeDetection class
   */
  private static AtomicLong idGenerator = new AtomicLong(1L);

  private static Map<Long, DeadCodeDetection> deadCodeDetectionByIdMap = new ConcurrentHashMap<>();

  public DeadCodeDetection create(String url){

    DeadCodeDetection deadCodeDetection = new DeadCodeDetection(url);
    deadCodeDetection.setId(idGenerator.getAndIncrement());

    deadCodeDetectionByIdMap.put(deadCodeDetection.getId(), deadCodeDetection);

    return deadCodeDetection;
  }

  public Optional<DeadCodeDetection> getById(Long id){
    return Optional.ofNullable(deadCodeDetectionByIdMap.get(id));
  }

  public Collection<DeadCodeDetection> listAll(){
    return deadCodeDetectionByIdMap.values();
  }

}
