package com.aurea.service.finder;

import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
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
        .filter(isNotUsed())
        .filter(notInAbstractMethod())
        .filter(hasNotImplicitDefinition())
        .map(entity -> DeadCodeFinder.getUnusedUnderstandEntity(entity, getType()))
        .collect(Collectors.toList());
  }

  private Predicate<Entity> notInAbstractMethod() {
    return entity -> entity.refs("definein", "method abstract", false).length == 0;
  }

  private Predicate<Entity> isNotUsed() {
    return entity -> entity.refs("useby", null, false).length == 0;
  }

  // In enum type Understand api shows a parameter having EnumType.valueOf.s definition
  // leading to erroneous detection. This filter is implemented to remove such cases
  private Predicate<Entity> hasNotImplicitDefinition() {
    return entity -> entity.refs("definein implicit", null, false).length == 0;
  }

  @Override
  public DeadCodeType getType() {
    return DeadCodeType.UNUSED_PARAMETER;
  }
}