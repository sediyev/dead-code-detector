package com.aurea.service.finder;

import static java.util.stream.Collectors.toList;

import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnusedFunctionFinder implements DeadCodeFinder{

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public List<UnusedUnderstandEntity> findAll(Database udb) {

    LOGGER.info("Processing algorithm :{}", getClass().getSimpleName());

    Entity[] privateVariables = udb.ents("private method ~constructor ~unresolved ~unknown");

    return Arrays.stream(privateVariables)
        .filter(entity -> entity.refs("callby", null, false).length == 0)
        .filter(this::notLambdaFunction)
        .map(entity -> DeadCodeFinder.getUnusedUnderstandEntity(entity, getType()))
        .collect(toList());
  }

  @Override
  public DeadCodeType getType() {
    return DeadCodeType.UNUSED_FUNCTION;
  }

  private boolean notLambdaFunction(Entity entity) {
    Boolean nameContainsLambda = entity.name().contains("(lambda");
    return ! nameContainsLambda ;
  }

}