package com.aurea.service.lookup;

import static java.util.stream.Collectors.toList;

import com.aurea.model.DeadCodeFinderType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnusedVariableFinder implements DeadCodeFinder {

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public List<UnusedUnderstandEntity> findAll(Database udb) {

    LOGGER.info("Processing algorithm :{}", getClass().getSimpleName());

    Entity[] privateVariables = udb.ents("private variable ~unresolved ~unknown");

    return Arrays.stream(privateVariables)
        .filter(entity -> entity.refs("useby", null, false).length == 0)
        .map(entity -> DeadCodeFinder.getUnusedUnderstandEntity(entity, getType()))
    .collect(toList());

  }

  @Override
  public DeadCodeFinderType getType() {
    return DeadCodeFinderType.UNUSED_VARIABLE;
  }

}
