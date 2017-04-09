package com.aurea.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;
import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.service.finder.AbstractDeadCodeFinderTest;
import com.scitools.understand.UnderstandException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class UnderstandServiceTest extends AbstractDeadCodeFinderTest {

  private DeadCodeDetection deadCodeDetection = new DeadCodeDetection();
  private static final String DEAD_CODE_ROOT = "deadcode";

  @Test
  public void findAll()
      throws UnderstandException, InterruptedException, IOException, TimeoutException {

    db = understandService.createAndGetUdbDatabase(getFullResourceFile(DEAD_CODE_ROOT), deadCodeDetection);

    understandService.findAndSetDeadCodes(deadCodeDetection, db, tempDir);
    List<UnusedUnderstandEntity> allDeadCodes = deadCodeDetection.getDeadCodeList();

    db.close();

    assertFalse(CollectionUtils.isEmpty(allDeadCodes));
    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.COMPLETED);

    allDeadCodes.forEach(System.out::println);
  }

}