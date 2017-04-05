package com.aurea.service.finder;

import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Understand;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class UnusedFunctionFinderTest extends AbstractDeadCodeFinderTest {

  @Test
  public void findAll() throws Exception {
    String deadVariablePath = "deadcode/function";

    understandService.createUdbDatabase(udbPath, getFullResourcePath(deadVariablePath));
    db = Understand.open(udbPath);

    DeadCodeFinder deadCodeFinder = new UnusedFunctionFinder();
    List<UnusedUnderstandEntity> unusedList = deadCodeFinder.findAll(db);

    db.close();
    Assert.assertTrue(unusedList.size() == 2);

    unusedList.forEach(System.out::println);
  }

  @Test
  public void getType() throws Exception {
    UnusedFunctionFinder finder = new UnusedFunctionFinder();
    Assert.assertTrue(finder.getType()== DeadCodeType.UNUSED_FUNCTION);
  }

}