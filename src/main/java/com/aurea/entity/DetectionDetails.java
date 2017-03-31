package com.aurea.entity;

import java.time.LocalDateTime;

public class DetectionDetails {


  private Long id;
  private RepositoryLanguage repositoryLanguage;
  private GitDetails gitDetails;
  private LocalDateTime executionStartTime;
  private LocalDateTime executionEndTime;

  // TODO either implement enum or state pattern here.
  //private ProgressionState currentState;

  public DetectionDetails(){

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RepositoryLanguage getRepositoryLanguage() {
    return repositoryLanguage;
  }

  public void setRepositoryLanguage(RepositoryLanguage repositoryLanguage) {
    this.repositoryLanguage = repositoryLanguage;
  }

  public LocalDateTime getExecutionStartTime() {
    return executionStartTime;
  }

  public void setExecutionStartTime(LocalDateTime executionStartTime) {
    this.executionStartTime = executionStartTime;
  }

  public LocalDateTime getExecutionEndTime() {
    return executionEndTime;
  }

  public void setExecutionEndTime(LocalDateTime executionEndTime) {
    this.executionEndTime = executionEndTime;
  }

  public GitDetails getGitDetails() {
    return gitDetails;
  }

  public void setGitDetails(GitDetails gitDetails) {
    this.gitDetails = gitDetails;
  }
}
