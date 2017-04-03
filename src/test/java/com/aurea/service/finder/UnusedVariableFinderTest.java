package com.aurea.service.finder;

import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.Assert;
import org.junit.Test;

public class UnusedVariableFinderTest extends AbstractDeadCodeFinderTest {

  @Test
  public void findAll()
      throws UnderstandException, InterruptedException, TimeoutException, IOException {
    String deadVariablePath = "deadcode/variable";

    understandService.createUdbDatabase(udbPath, getFullResourcePath(deadVariablePath));
    db = Understand.open(udbPath);

    DeadCodeFinder deadCodeFinder = new UnusedVariableFinder();
    List<UnusedUnderstandEntity> unusedList = deadCodeFinder.findAll(db);

    db.close();

    unusedList.forEach(System.out::println);
  }

  @Test
  public void getType() throws Exception {
    UnusedVariableFinder finder = new UnusedVariableFinder();
    Assert.assertTrue(finder.getType() == DeadCodeType.UNUSED_VARIABLE);
  }

}