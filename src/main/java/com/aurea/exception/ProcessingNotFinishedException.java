package com.aurea.exception;

import com.aurea.model.DeadCodeDetectionStatus;
import javax.ws.rs.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProcessingNotFinishedException extends NotFoundException {
  public ProcessingNotFinishedException(DeadCodeDetectionStatus status){
    super("Repository processing is not finished. Current status: " + status.name());
  }

}
