package com.aurea.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

  @RequestMapping(value = "/ping", method = GET)
  public String ping() {
    return "OK";
  }
}
