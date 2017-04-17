package com.aurea.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.beans.Transient;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ws.rs.Produces;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces("application/json")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GitHubRepo {

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private URL repoUrl;
  private String branch;

  GitHubRepo(String urlValue, String branch) {

    this.repoUrl = parseURL(urlValue);
    this.branch = StringUtils.isBlank(branch) ? null : branch;
  }

  private URL parseURL(String urlValue) {
    try {
      return new URL(urlValue);
    } catch (MalformedURLException e) {
      LOGGER.error("error parsing url: " + urlValue, e);
      throw new IllegalArgumentException("Input: " + urlValue + ", is not a valid url.");
    }
  }

  @JsonProperty("url")
  public URL getRepoUrl() {
    return repoUrl;
  }

  @Transient
  public String getUrlValue() {
    return repoUrl.toString();
  }

  public String getBranch() {
    return branch;
  }

}
