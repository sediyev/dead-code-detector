package com.aurea.service.lookup;

import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.model.DeadCodeFinderType;
import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import java.util.Arrays;
import java.util.List;

public interface DeadCodeFinder {
  public List<UnusedUnderstandEntity> findAll(Database udb);

  public DeadCodeFinderType getType();

  public static UnusedUnderstandEntity getUnusedUnderstandEntity(Entity entity, DeadCodeFinderType finderType) {
    UnusedUnderstandEntity unusedEntity = new UnusedUnderstandEntity();
    unusedEntity.setDeadCodeFinderType(finderType);
    unusedEntity.setName(entity.name());
    unusedEntity.setKind(entity.kind().name());

    Reference ref = Arrays
        .stream(entity.refs("definedin", null, false))
        .findFirst()
        .orElse(null);

    unusedEntity.setColumn(ref.column());
    unusedEntity.setLine(ref.line());

    unusedEntity.setFile(ref.file().longname(true));

    return unusedEntity;
  }
}
