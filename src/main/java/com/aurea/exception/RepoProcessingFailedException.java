package com.aurea.exception;

import javax.ws.rs.WebApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Repository processing failed.")
public class RepoProcessingFailedException extends WebApplicationException{
  public RepoProcessingFailedException(String errorMessage){
    super(errorMessage, 410);
  }

}
