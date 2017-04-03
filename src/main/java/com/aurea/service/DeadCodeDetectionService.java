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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DeadCodeDetectionService {

  /**
   * Used to create unique id for each deadCodeDetection class
   */
  private static AtomicLong idGenerator = new AtomicLong(1L);

  private static final int initialHashMapCapacity = 1_000;
  private static Map<Long, DeadCodeDetection> deadCodeDetectionByIdMap = new ConcurrentHashMap<>(initialHashMapCapacity);

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

  public Map<DeadCodeDetectionStatus, Long> summary() {
    return deadCodeDetectionByIdMap.values()
        .stream()
        .collect(Collectors
            .groupingBy(DeadCodeDetection::getDeadCodeDetectionStatus, Collectors.counting()));
  }

  public List<UnusedUnderstandEntity> get(Long id, DeadCodeType deadCodeType) {
    List<UnusedUnderstandEntity> entityList = get(id);

    return entityList.stream()
        .filter(unusedUnderstandEntity -> unusedUnderstandEntity.getDeadCodeType() == deadCodeType)
        .collect(Collectors.toList());
  }

  // Filters map with deadCodeDetectionStatus
  private Map<Long, DeadCodeDetection> applyStatusFilter(
      DeadCodeDetectionStatus deadCodeDetectionStatus, Map<Long, DeadCodeDetection> map) {

    if (deadCodeDetectionStatus == null) {
      return map;
    }

    return map.values().stream()
        .filter(deadCodeDetection -> deadCodeDetection.getDeadCodeDetectionStatus()
            == deadCodeDetectionStatus)
        .collect(Collectors.toMap(DeadCodeDetection::getId, Function.identity()));
  }

  // Filters map with url
  private Map<Long, DeadCodeDetection> applyUrlFilter(String url,
      Map<Long, DeadCodeDetection> map) {

    if (StringUtils.isBlank(url)) {
      return map;
    }

    return map.values().stream()
        .filter(deadCodeDetection -> StringUtils
            .equals(deadCodeDetection.getGitHubRepoUrl().toString(), url))
        .collect(Collectors.toMap(DeadCodeDetection::getId, Function.identity()));
  }

  public Collection<DeadCodeDetection> listAll(int page, int maxCount,
      DeadCodeDetectionStatus deadCodeDetectionStatus, String url) {

    Map<Long, DeadCodeDetection> filteredByUrlMap = applyUrlFilter(url, deadCodeDetectionByIdMap);
    Map<Long, DeadCodeDetection> filteredByStatusAndUrlMap = applyStatusFilter(
        deadCodeDetectionStatus, filteredByUrlMap);

    return getDeadCodeDetectionList(filteredByStatusAndUrlMap, page, maxCount);
  }

  private Collection<DeadCodeDetection> getDeadCodeDetectionList(
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
