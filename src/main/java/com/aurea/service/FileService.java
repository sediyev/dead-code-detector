package com.aurea.service;

import java.io.File;
import java.io.IOException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

@Service
class FileService {

  /**
   * This method will not fail in case of IOException
   * @param directory directory path to be deleted
   */
  void tryToCleanRootDirectory(File directory){

    if(directory == null){
      return;
    }

    try {
      FileUtils.cleanDirectory(directory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  String getUdbPath(File sourcePath){
    return sourcePath.getAbsolutePath() + "\\db.udb";
  }
}
