package com.aurea.exception;

import javax.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Repository not found.")
public class RepoNotFoundException extends NotFoundException {

  public RepoNotFoundException() {
    super();
  }

}
