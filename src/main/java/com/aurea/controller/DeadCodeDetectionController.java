package com.aurea.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.aurea.entity.DetectionDetails;
import com.aurea.entity.GitDetails;
import com.aurea.entity.RepositoryLanguage;
import com.aurea.service.ExecutorService;
import java.lang.invoke.MethodHandles;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/v1")
public class DeadCodeDetectionController {

  private final ExecutorService executorService;

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public DeadCodeDetectionController(ExecutorService executorService) {
    this.executorService = executorService;
  }

  @ResponseBody
  @RequestMapping(value = "/add", method = POST, produces = APPLICATION_JSON_VALUE)
  public String addRepositoryForInspection(@RequestParam String url, @RequestParam
      RepositoryLanguage language) {

    LOGGER.info("Rest call to add entity for inspection. url: {}, repositoryLanguage: {}", url,
        language);

    DetectionDetails detectionDetails = new DetectionDetails();
    detectionDetails.setRepositoryLanguage(language);

    GitDetails gitDetails = new GitDetails();
    gitDetails.setRepoUrl(url);
    detectionDetails.setGitDetails(gitDetails);

    try {
      executorService.executeDeadCodeDetection(detectionDetails);
    } catch (GitAPIException e) {
      LOGGER.error("Error cloning entity", e);
      // TODO
      // set state to failed.
    }

    throw new RuntimeException("Not yet implemented!");
  }

  @ResponseBody
  @RequestMapping(value = "/repositories", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRepositories() {

    LOGGER.info("Rest call to list all repositories");

    return ResponseEntity.ok("Not implemented yet");
  }

  @ResponseBody
  @RequestMapping(value = "/repositories/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRepositoryById(@PathVariable("id") String id) {

    return ResponseEntity.ok("Not Yet Implemented.");
  }

}
