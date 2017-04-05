package com.aurea.service;

import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.service.finder.AbstractDeadCodeFinderTest;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class UnderstandServiceTest extends AbstractDeadCodeFinderTest {

  @Test
  public void findAll()
      throws UnderstandException, InterruptedException, IOException, TimeoutException {

    String deadCodeRoot = "deadcode";

    understandService.createUdbDatabase(udbPath, getFullResourcePath(deadCodeRoot));
    db = Understand.open(udbPath);

    List<UnusedUnderstandEntity> allDeadCodes = understandService
        .findAllDeadCode(db, tempDir.getAbsolutePath());

    db.close();

    Assert.assertNotNull(allDeadCodes);
    Assert.assertFalse(CollectionUtils.isEmpty(allDeadCodes));

    allDeadCodes.forEach(System.out::println);
  }

}