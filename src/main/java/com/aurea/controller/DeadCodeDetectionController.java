package com.aurea.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.DeadCodeDetectionStatus;
import com.aurea.model.DeadCodeType;
import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.service.DeadCodeDetectionService;
import com.aurea.service.ExecutorService;
import io.swagger.annotations.ApiOperation;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deadcode/")
public class DeadCodeDetectionController {

  private final ExecutorService executorService;
  private final DeadCodeDetectionService deadCodeDetectionService;

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public DeadCodeDetectionController(ExecutorService executorService,
      DeadCodeDetectionService deadCodeDetectionService) {
    this.executorService = executorService;
    this.deadCodeDetectionService = deadCodeDetectionService;
  }

  @ApiOperation(value = "Adds github repository for inspection",
      notes = "Creates and returns new object to be inspected and starts processing it in the background.",
      response = DeadCodeDetection.class)
  @ResponseBody
  @RequestMapping(value = "/repositories", method = POST, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeadCodeDetection> addRepositoryForInspection(@RequestParam String url) {

    LOGGER.info("Rest call to add model for inspection. url: {}", url);

    DeadCodeDetection deadCodeDetection = deadCodeDetectionService.create(url);
    executorService.executeDeadCodeDetection(deadCodeDetection);

    return new ResponseEntity<>(deadCodeDetection, HttpStatus.CREATED);
  }

  @ApiOperation(value = "Lists all dead-code occurrences",
      notes = "Searches for dead code occurrences with the given filters and returns data with paging.",
      response = DeadCodeDetection.class,
      responseContainer = "List")
  @ResponseBody
  @RequestMapping(value = "/repositories", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<DeadCodeDetection>> getRepositories(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "50") Integer limit,
      @RequestParam(required = false) DeadCodeDetectionStatus status,
      @RequestParam(required = false) String repoUrl)

  {

    if (limit < 1 || limit > 1000) {
      limit = 1000;
    }
    if (page < 0) {
      page = 0;
    }

    LOGGER.info("Rest call to list all repositories. page: {}, maxCount: {}, repoUrl: {}, status: {}", page,
        limit, repoUrl, status);

    return new ResponseEntity<>(
        deadCodeDetectionService.listAll(page, limit, status, repoUrl),
        HttpStatus.OK);
  }

  @ApiOperation(value = "Retrieve the repository with the given id.",
      notes = "In addition to getting single repository, status filters are available to sort the data.",
      response = UnusedUnderstandEntity.class,
      responseContainer = "List")
  @ResponseBody
  @RequestMapping(value = "/repositories/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<UnusedUnderstandEntity>> getRepositoryById(@PathVariable("id") Long id,
      @RequestParam(required = false) DeadCodeType deadCodeType) {

    LOGGER.info("Rest call to list repository by id: {}, filter: {}", id, deadCodeType);

    if (deadCodeType == null) {
      return new ResponseEntity<>(deadCodeDetectionService.get(id), HttpStatus.FOUND);
    }

    return new ResponseEntity<>(deadCodeDetectionService.get(id, deadCodeType), HttpStatus.FOUND);
  }

  @ApiOperation(value = "Lists frequency of each state filter",
      notes = "Groups repositories by their current state and returns it.")
  @ResponseBody
  @RequestMapping(value = "/repositories/summary", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<DeadCodeDetectionStatus, Long>> summary() {

    LOGGER.info("Rest call to list repository summary");

    return new ResponseEntity<>(deadCodeDetectionService.summary(), HttpStatus.FOUND);
  }

}
