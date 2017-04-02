package com.aurea.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.aurea.model.DeadCodeDetection;
import com.aurea.service.DeadCodeDetectionService;
import com.aurea.service.ExecutorService;
import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

  @ResponseBody
  @RequestMapping(value = "/add", method = POST, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeadCodeDetection> addRepositoryForInspection(@RequestParam String url){

    LOGGER.info("Rest call to add model for inspection. url: {}", url);

    DeadCodeDetection deadCodeDetection = deadCodeDetectionService.create(url);
    executorService.executeDeadCodeDetection(deadCodeDetection);

    return new ResponseEntity<>(deadCodeDetection, HttpStatus.OK);
  }

  @ResponseBody
  @RequestMapping(value = "/repositories", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<DeadCodeDetection>> getRepositories() {

    LOGGER.info("Rest call to list all repositories");

    return new ResponseEntity<>(deadCodeDetectionService.listAll(),HttpStatus.OK);
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  @RequestMapping(value = "/repositories/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<DeadCodeDetection> getRepositoryById(@PathVariable("id") String id) {

    LOGGER.info("Rest call to list repository by id: {}", id);

    Optional<DeadCodeDetection> deadCodeDetection = deadCodeDetectionService.getById(new Long(id));

    return new ResponseEntity<>(deadCodeDetection.get(), HttpStatus.FOUND);
  }

}
