package com.aurea.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by sediy on 3/28/2017.
 */
@Component
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
    public ResponseEntity<String> getRepositories(@PathVariable("id") String id) {

        return ResponseEntity.ok("It Will return repository detection results belonging to specific id. Not implemented yet");
    }

}
