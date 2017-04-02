package com.aurea.model;

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

  public URL getRepoUrl() {
    return repoUrl;
  }

  public void setRepoUrl(URL repoUrl) {
    this.repoUrl = repoUrl;
  }
}
