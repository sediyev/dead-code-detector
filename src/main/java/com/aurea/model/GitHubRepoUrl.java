package com.aurea.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.MalformedURLException;
import java.net.URL;

public class GitHubRepoUrl {

  private URL repoUrl;

  GitHubRepoUrl(String urlValue) {

    this.repoUrl = parseURL(urlValue);
  }


  private URL parseURL(String urlValue) {
    try {
      return new URL(urlValue);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Input: " + urlValue + ", is not a valid url.");
    }
  }

  @JsonProperty("url")
  public URL getRepoUrl() {
    return repoUrl;
  }

  public void setRepoUrl(URL repoUrl) {
    this.repoUrl = repoUrl;
  }

  @Override
  public String toString(){
    return repoUrl.toString();
  }
}
