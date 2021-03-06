package com.aurea.model;

import com.aurea.model.state.InitialState;
import com.aurea.model.state.StateConsumer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.ws.rs.Produces;
import org.apache.commons.lang3.Validate;

@Produces("application/json")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeadCodeDetection implements Serializable{

  private Long id;
  private GitHubRepo gitHubRepo;
  private LocalDateTime timeRepoIsAdded;
  private LocalDateTime executionStartTime;
  private LocalDateTime executionEndTime;

  private DeadCodeDetectionStatus deadCodeDetectionStatus;

  private List<UnusedUnderstandEntity> deadCodeList;

  private String errorMessage;

  public DeadCodeDetection(){
  }

  public DeadCodeDetection(String urlValue, String branch) {

    Validate.notBlank(urlValue, "Url cannot be empty!");
    this.gitHubRepo = new GitHubRepo(urlValue, branch);

    this.setState(new InitialState());

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  public LocalDateTime getExecutionStartTime() {
    return executionStartTime;
  }

  public void setExecutionStartTime(LocalDateTime executionStartTime) {
    this.executionStartTime = executionStartTime;
  }

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  public LocalDateTime getExecutionEndTime() {
    return executionEndTime;
  }

  public void setExecutionEndTime(LocalDateTime executionEndTime) {
    this.executionEndTime = executionEndTime;
  }

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  public LocalDateTime getTimeRepoIsAdded() {
    return timeRepoIsAdded;
  }

  public void setTimeRepoIsAdded(LocalDateTime timeRepoIsAdded) {
    this.timeRepoIsAdded = timeRepoIsAdded;
  }

  public GitHubRepo getGitHubRepo() {
    return gitHubRepo;
  }

  @JsonProperty("status")
  public DeadCodeDetectionStatus getDeadCodeDetectionStatus() {
    return deadCodeDetectionStatus;
  }

  public void setDeadCodeDetectionStatus(DeadCodeDetectionStatus deadCodeDetectionStatus) {
    this.deadCodeDetectionStatus = deadCodeDetectionStatus;
  }

  public void setState(StateConsumer stateConsumer){
    stateConsumer.accept(this);
  }

  @JsonIgnore
  public List<UnusedUnderstandEntity> getDeadCodeList() {
    return deadCodeList;
  }

  public void setDeadCodeList(List<UnusedUnderstandEntity> deadCodeList) {
    this.deadCodeList = deadCodeList;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
