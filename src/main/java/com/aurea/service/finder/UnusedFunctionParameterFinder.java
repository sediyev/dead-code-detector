package com.aurea.service.finder;

import static com.aurea.service.finder.FinderFilters.hasNotImplicitDefinition;
import static com.aurea.service.finder.FinderFilters.notUsed;
import static com.aurea.service.finder.FinderFilters.notInAbstractMethod;

import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnusedFunctionParameterFinder implements DeadCodeFinder {

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public List<UnusedUnderstandEntity> findAll(Database udb) {

    LOGGER.info("Processing algorithm :{}", getClass().getSimpleName());

    Entity[] parameters = udb.ents("parameter ~unresolved ~unknown");

    return Arrays.stream(parameters)
        .filter(notUsed())
        .filter(notInAbstractMethod())
        .filter(hasNotImplicitDefinition())
        .map(entity -> DeadCodeFinder.getUnusedUnderstandEntity(entity, getType()))
        .collect(Collectors.toList());
  }

  @Override
  public DeadCodeType getType() {
    return DeadCodeType.UNUSED_PARAMETER;
  }
}
