package com.aurea.exception;

import javax.ws.rs.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class GitHubDownloadException extends BadRequestException{

  public GitHubDownloadException(String message){
    super(message);
  }
}
