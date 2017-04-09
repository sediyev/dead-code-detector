package com.aurea.util;

import java.io.File;
import java.io.IOException;

public class FileUtils extends org.apache.tomcat.util.http.fileupload.FileUtils{

  private static final String UDB_FILE_NAME = "database.udb";

  /**
   * This method will not fail in case of IOException
   * @param directory directory path to be deleted
   */
  public static void tryToCleanRootDirectory(File directory){

    if(directory == null){
      return;
    }

    try {
      cleanDirectory(directory);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getUdbPath(File sourcePath){
    File udbFile = new File(sourcePath, UDB_FILE_NAME);
    return udbFile.getAbsolutePath();
  }
}
