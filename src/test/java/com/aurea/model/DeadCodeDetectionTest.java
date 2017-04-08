package com.aurea.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DeadCodeDetectionTest {

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  public void unusedUnderstandEntityListIsJsonIgnored() throws IOException {

    DeadCodeDetection deadCodeDetection = new DeadCodeDetection();
    deadCodeDetection.setDeadCodeList(getUnusedUnderstandEntityList());

    assertThat(deadCodeDetection.getDeadCodeList()).isNotEmpty();

    String json = mapper.writeValueAsString(deadCodeDetection);

    DeadCodeDetection deSerializedDeadCodeDetection = mapper
        .readValue(json, DeadCodeDetection.class);

    assertThat(deSerializedDeadCodeDetection.getDeadCodeList()).isNull();
  }

  private List<UnusedUnderstandEntity> getUnusedUnderstandEntityList() {
    UnusedUnderstandEntity entity = new UnusedUnderstandEntity();
    entity.setFile("Test.java");
    entity.setName("Test Name");
    entity.setLine(5);
    entity.setColumn(10);
    entity.setDeadCodeType(DeadCodeType.UNUSED_FUNCTION);
    return Collections.singletonList(entity);
  }
}