package com.aurea.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class UnusedUnderstandEntity {

  private DeadCodeType deadCodeType;

  private String file;
  private String name;
  private String kind;
  private Integer line;
  private Integer column;

  public DeadCodeType getDeadCodeType() {
    return deadCodeType;
  }

  public void setDeadCodeType(
      DeadCodeType deadCodeType) {
    this.deadCodeType = deadCodeType;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public Integer getLine() {
    return line;
  }

  public void setLine(Integer line) {
    this.line = line;
  }

  public Integer getColumn() {
    return column;
  }

  public void setColumn(Integer column) {
    this.column = column;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this,
        ToStringStyle.JSON_STYLE, true, true);
  }
}
