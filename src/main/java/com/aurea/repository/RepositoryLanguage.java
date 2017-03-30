package com.aurea.repository;

public enum RepositoryLanguage {
  JAVA("java"), C_PLUS_PLUS("c++"), C_SHARP("c#"), ADA("ada");

  private String name;

  private RepositoryLanguage(String name){
    this.name = name;
  }

  public String getName(){
    return this.name;
  }
}
