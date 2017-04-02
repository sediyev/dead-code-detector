package com.aurea.service.lookup;

import com.aurea.model.DeadCodeFinderType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Understand;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class UnusedFunctionParameterFinderTest extends AbstractDeadCodeFinderTest{

  @Test
  public void findAll() throws Exception {
    String deadVariablePath = "dead/parameter";

    understandService.createUdb(udbPath, getFullResourcePath(deadVariablePath));
    db = Understand.open(udbPath);

    DeadCodeFinder deadCodeFinder = new UnusedFunctionParameterFinder();
    List<UnusedUnderstandEntity> unusedList = deadCodeFinder.findAll(db);

    db.close();

    System.out.println("Unused list size: "+ unusedList.size());
    unusedList.forEach(System.out::println);
  }

  @Test
  public void getType() throws Exception {
    UnusedFunctionParameterFinder finder = new UnusedFunctionParameterFinder();
    Assert.assertTrue(finder.getType()== DeadCodeFinderType.UNUSED_PARAMETER);
  }
}