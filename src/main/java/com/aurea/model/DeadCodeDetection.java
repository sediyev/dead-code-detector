package com.aurea.model;

import com.aurea.model.state.InitialState;
import com.aurea.model.state.StateConsumer;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class DeadCodeDetection implements Serializable{

  private Long id;
  private GitHubRepoUrl gitHubRepoUrl;
  private LocalDateTime timeRepoIsAdded;
  private LocalDateTime executionStartTime;
  private LocalDateTime executionEndTime;

  private DeadCodeDetectionStatus deadCodeDetectionStatus;

  private List<UnusedUnderstandEntity> deadCodeList;

  public DeadCodeDetection(String urlValue) {

    Validate.notBlank(urlValue, "Url cannot be empty!");
    this.gitHubRepoUrl = new GitHubRepoUrl(urlValue);

    this.setState(new InitialState());

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonFormat(pattern="yyyy-MM-dd")
  public LocalDateTime getExecutionStartTime() {
    return executionStartTime;
  }

  public void setExecutionStartTime(LocalDateTime executionStartTime) {
    this.executionStartTime = executionStartTime;
  }

  @JsonFormat(pattern="yyyy-MM-dd")
  public LocalDateTime getExecutionEndTime() {
    return executionEndTime;
  }

  public void setExecutionEndTime(LocalDateTime executionEndTime) {
    this.executionEndTime = executionEndTime;
  }

  @JsonFormat(pattern="yyyy-MM-dd")
  public LocalDateTime getTimeRepoIsAdded() {
    return timeRepoIsAdded;
  }

  public void setTimeRepoIsAdded(LocalDateTime timeRepoIsAdded) {
    this.timeRepoIsAdded = timeRepoIsAdded;
  }

  public GitHubRepoUrl getGitHubRepoUrl() {
    return gitHubRepoUrl;
  }

  public void setGitHubRepoUrl(GitHubRepoUrl gitHubRepoUrl) {
    this.gitHubRepoUrl = gitHubRepoUrl;
  }

  public DeadCodeDetectionStatus getDeadCodeDetectionStatus() {
    return deadCodeDetectionStatus;
  }

  public void setDeadCodeDetectionStatus(DeadCodeDetectionStatus deadCodeDetectionStatus) {
    this.deadCodeDetectionStatus = deadCodeDetectionStatus;
  }

  public void setState(StateConsumer stateConsumer){
    stateConsumer.accept(this);
  }

  public List<UnusedUnderstandEntity> getDeadCodeList() {
    return deadCodeList;
  }

  public void setDeadCodeList(List<UnusedUnderstandEntity> deadCodeList) {
    this.deadCodeList = deadCodeList;
  }

}
