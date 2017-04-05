package com.aurea.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.aurea.util.AbstractDeadCodeDetectionTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GitHubRepoUrlTest extends AbstractDeadCodeDetectionTest{

  private final String invalidUrl = "invalid";
  private final String notAValidUrlMessage = "not a valid url";

  @Test
  public void invalidUrlTest(){
    assertThatThrownBy(() -> new GitHubRepoUrl(invalidUrl))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(notAValidUrlMessage);
  }

  @Test
  public void toStringTest(){
    GitHubRepoUrl gitHubRepoUrl = new GitHubRepoUrl(repoUrl);

    assertThat(gitHubRepoUrl.toString()).isEqualTo(repoUrl);
  }


}