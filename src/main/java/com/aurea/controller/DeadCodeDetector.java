package com.aurea.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sediy on 3/28/2017.
 */
@RestController
@RequestMapping("/rest/v1")
public class DeadCodeDetector {

  @ResponseBody
  @RequestMapping(value = "/add", method = POST, produces = APPLICATION_JSON_VALUE)
  public String addRepositoryForInspection(@RequestParam String url) {

    throw new RuntimeException("Not yet implemented!");
  }

  @ResponseBody
  @RequestMapping(value = "/repositories", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRepositories() {

    return ResponseEntity.ok("Not implemented yet");
  }

  @ResponseBody
  @RequestMapping(value = "/repositories/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRepositoryById(@PathVariable("id") String id) {

    return ResponseEntity.ok("Not Yet Implemented.");
  }

}
