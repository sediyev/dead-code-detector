package com.aurea.model.state;

import static org.assertj.core.api.Assertions.assertThat;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;
import com.aurea.util.AbstractDeadCodeDetectionTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StateConsumerTest extends AbstractDeadCodeDetectionTest{

  private DeadCodeDetection deadCodeDetection = new DeadCodeDetection(repoUrl);

  private final String codeDetectionFailedMessage = "Code Detection Failed";

  @Test
  public void testInitialState(){

    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.ADDED);

    assertThat(deadCodeDetection.getTimeRepoIsAdded()).isNotNull();
  }

  @Test
  public void testDownloadingRepoState(){
    deadCodeDetection.setState(new DownloadingRepoState());

    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.DOWNLOADING_REPO);

  }

  @Test
  public void failedState(){
    deadCodeDetection.setState(new FailedState(codeDetectionFailedMessage));

    assertThat(deadCodeDetection.getErrorMessage()).isEqualTo(codeDetectionFailedMessage);

    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.FAILED);
  }

  @Test
  public void processingState(){

    deadCodeDetection.setState(new ProcessingState());

    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.PROCESSING);

    assertThat(deadCodeDetection.getExecutionStartTime()).isNotNull();

  }

  @Test
  public void completedState(){
    deadCodeDetection.setState(new CompletedState());

    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.COMPLETED);

    assertThat(deadCodeDetection.getExecutionEndTime()).isNotNull();

  }

  @Test
  public void waitingInQueueState(){
    deadCodeDetection.setState(new WaitingInQueueState());

    assertThat(deadCodeDetection.getDeadCodeDetectionStatus()).isEqualTo(
        DeadCodeDetectionStatus.IN_QUEUE);
  }

}