package com.aurea.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnSupportedLanguageException extends RuntimeException {

  public UnSupportedLanguageException(String message) {
    super(message);
  }

}
