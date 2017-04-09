package com.aurea.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import com.aurea.model.DeadCodeDetectionStatus;
import com.aurea.util.AbstractDeadCodeDetectionTest;
import com.scitools.understand.UnderstandException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.zeroturnaround.exec.InvalidExitValueException;

@RunWith(MockitoJUnitRunner.class)
public class ExecutorServiceTest extends AbstractDeadCodeDetectionTest{

  @Mock
  private UnderstandService understandService;

  @Mock
  private GitService gitService;

  private ExecutorService executorService;

  @Before
  public void setup() {
    executorService = Mockito.spy(new ExecutorService(gitService, understandService));
  }

  @Test
  public void executeDeadCodeDetectionThrowsTimeOutException()
      throws InterruptedException, TimeoutException, UnderstandException, IOException {

    doThrow(TimeoutException.class).when(understandService).createAndGetUdbDatabase(null, deadCodeDetection);

    executorService.executeDeadCodeDetection(deadCodeDetection, null);

    Assertions.assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.FAILED);
  }

  @Test
  public void executeDeadCodeDetectionThrowsInvalidExitValueException()
      throws InterruptedException, TimeoutException, UnderstandException, IOException {

    doThrow(InvalidExitValueException.class).when(understandService).createAndGetUdbDatabase(null, deadCodeDetection);

    executorService.executeDeadCodeDetection(deadCodeDetection, null);

    Assertions.assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.FAILED);
  }

  @Test
  public void executeDeadCodeDetectionThrowsException()
      throws InterruptedException, TimeoutException, UnderstandException, IOException {

    doThrow(Exception.class).when(understandService).createAndGetUdbDatabase(null, deadCodeDetection);

    executorService.executeDeadCodeDetection(deadCodeDetection, null);

    Assertions.assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.FAILED);
  }


}