package com.aurea.service;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;
import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DeadCodeDetectionService {

  /**
   * Used to create unique id for each deadCodeDetection class
   */
  private static AtomicLong idGenerator = new AtomicLong(1L);

  private static Map<Long, DeadCodeDetection> deadCodeDetectionByIdMap = new ConcurrentHashMap<>();

  public DeadCodeDetection create(String url) {

    DeadCodeDetection deadCodeDetection = new DeadCodeDetection(url);
    deadCodeDetection.setId(idGenerator.getAndIncrement());

    deadCodeDetectionByIdMap.put(deadCodeDetection.getId(), deadCodeDetection);

    return deadCodeDetection;
  }

  public List<UnusedUnderstandEntity> get(Long id) {
    DeadCodeDetection deadCodeDetection = deadCodeDetectionByIdMap.get(id);
    return deadCodeDetection == null ? new ArrayList<>() : deadCodeDetection.getDeadCodeList();
  }

  public List<UnusedUnderstandEntity> get(Long id, DeadCodeType deadCodeType) {
    List<UnusedUnderstandEntity> entityList = get(id);

    return entityList.stream()
        .filter(unusedUnderstandEntity -> unusedUnderstandEntity.getDeadCodeType() == deadCodeType)
        .collect(Collectors.toList());
  }

  public Collection<DeadCodeDetection> listAll(int page, int maxCount,
      DeadCodeDetectionStatus deadCodeDetectionStatus) {

    if (deadCodeDetectionStatus == null) {
      return getDeadCodeDetections(deadCodeDetectionByIdMap, page, maxCount);
    }

    Map<Long, DeadCodeDetection> filteredMap =
        deadCodeDetectionByIdMap.values().stream()
            .filter(deadCodeDetection -> deadCodeDetection.getDeadCodeDetectionStatus()
                == deadCodeDetectionStatus)
            .collect(Collectors.toMap(DeadCodeDetection::getId, Function.identity()));

    return getDeadCodeDetections(filteredMap, page, maxCount);
  }

  private Collection<DeadCodeDetection> getDeadCodeDetections(
      Map<Long, DeadCodeDetection> deadCodeDetectionMap, int page, int maxCount) {
    int listSize = deadCodeDetectionMap.size();

    int adjustedPage = Math.min(listSize / maxCount, page);

    int pagingStart = adjustedPage * maxCount;
    int pagingEnd = Math.min(maxCount * (adjustedPage + 1), listSize);

    return deadCodeDetectionMap.values()
        .stream()
        .skip(pagingStart)
        .limit(pagingEnd)
        .collect(Collectors.toList());
  }

}
