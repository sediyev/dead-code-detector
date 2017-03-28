package com.aurea.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.Collections;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by sediy on 3/28/2017.
 */
@RestController
@RequestMapping("/rest/v1/")
public class DeadCodeDetector {

    @ResponseBody
    @RequestMapping(value="/add", method = POST, produces = APPLICATION_JSON)
    public String addRepositoryForInspection(@RequestParam String url){

        throw new RuntimeException("Not yet implemented!");
    }

    @ResponseBody
    @RequestMapping(value="/repositories", method = GET, produces = APPLICATION_JSON)
    public ResponseEntity<String> getRepositories(){

        return ResponseEntity.ok("Not implemented yet");
    }

    @ResponseBody
    @RequestMapping(value="/repositories/{id}", method = GET, produces = APPLICATION_JSON)
    public ResponseEntity<String> getRepositories(@PathVariable("id") String id){

        return ResponseEntity.ok("It Will return repository detection results belonging to specific id. Not implemented yet");
    }

}
