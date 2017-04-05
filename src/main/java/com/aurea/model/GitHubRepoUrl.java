package com.aurea.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubRepoUrl {

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private URL repoUrl;

  GitHubRepoUrl(String urlValue) {

    this.repoUrl = parseURL(urlValue);
  }

  private URL parseURL(String urlValue) {
    try {
      return new URL(urlValue);
    } catch (MalformedURLException e) {
      LOGGER.error("error parsing url: "+urlValue, e);
      throw new IllegalArgumentException("Input: " + urlValue + ", is not a valid url.");
    }
  }

  @JsonProperty("url")
  public URL getRepoUrl() {
    return repoUrl;
  }

  @Override
  public String toString(){
    return repoUrl.toString();
  }
}
