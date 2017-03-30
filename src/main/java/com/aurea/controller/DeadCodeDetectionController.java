package com.aurea.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.aurea.service.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public DeadCodeDetectionController(ExecutorService executorService){
    this.executorService = executorService;
  }

  @ResponseBody
  @RequestMapping(value = "/add", method = POST, produces = APPLICATION_JSON_VALUE)
  public String addRepositoryForInspection(@RequestParam String url) {

    throw new RuntimeException("Not yet implemented!");
  }

  @ResponseBody
  @RequestMapping(value = "/repositories", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRepositories() {

    executorService.executeDeadCodeDetection();

    return ResponseEntity.ok("Not implemented yet");
  }

  @ResponseBody
  @RequestMapping(value = "/repositories/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRepositoryById(@PathVariable("id") String id) {

    return ResponseEntity.ok("Not Yet Implemented.");
  }

}
