package com.aurea.service.lookup;

import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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

//    Entity[] parameters = udb.ents("parameter ~unresolved ~unknown");
//
//    return Arrays.stream(parameters)
//        .filter(entity -> entity.refs("useby", null, false).length == 0)
//        .map(entity -> DeadCodeFinder.getUnusedUnderstandEntity(entity, getType()))
//        .collect(toList());

      Entity[] parameters = udb.ents("type ~interface ~unresolved ~unknown");


      //TODO unacceptable code
      List<UnusedUnderstandEntity> entityList = new ArrayList<>();
      Arrays.stream(parameters).forEach(entity -> {
        Arrays.stream(entity.refs("~unresolved ~unknown", "method", true))
        .filter(reference -> {
          Entity methodEntity = reference.ent();

          if(getRef(methodEntity) == null && getRef(methodEntity).line() != getRef(entity).line()){
            entityList.addAll(
                Arrays.stream(methodEntity.refs("~unresolved ~unknown ~catch", "parameter", true))
                .filter(reference1 -> reference1.ent().refs("useby", null, false).length == 0)
                    .map(reference1 -> reference1.ent())
                    .map(entity1 -> DeadCodeFinder.getUnusedUnderstandEntity(entity1, getType()))
                .collect(Collectors.toList())
            );
          }
          return true;
        });

      });

      return entityList;

  }

  private Reference getRef(Entity methodEntity){
    Reference[] defineIn = methodEntity.refs("definein", null, false);
    return defineIn.length >0 ? defineIn[0] : null;
  }

  @Override
  public DeadCodeType getType() {
    return DeadCodeType.UNUSED_PARAMETER;
  }
}
