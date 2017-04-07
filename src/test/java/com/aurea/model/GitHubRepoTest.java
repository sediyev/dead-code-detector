package com.aurea.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aurea.util.AbstractDeadCodeDetectionTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GitHubRepoTest extends AbstractDeadCodeDetectionTest{

  private final String invalidUrl = "invalid";
  private final static String BRANCH = "master";
  private final String notAValidUrlMessage = "not a valid url";

  @Test
  public void invalidUrlTest(){
    assertThatThrownBy(() -> new GitHubRepo(invalidUrl, BRANCH))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(notAValidUrlMessage);
  }

  @Test
  public void toStringTest(){
    GitHubRepo gitHubRepo = new GitHubRepo(repoUrl, BRANCH);

    assertThat(gitHubRepo.getUrlValue()).isEqualTo(repoUrl);
  }


}