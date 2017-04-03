package com.aurea.service.finder;

import com.aurea.service.UnderstandService;
import com.google.common.io.Files;
import com.scitools.understand.Database;
import com.scitools.understand.UnderstandException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import org.eclipse.jgit.util.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ReflectionUtils;

@SpringBootTest
public abstract class AbstractDeadCodeFinderTest {

  protected UnderstandService understandService = new UnderstandService();

  protected String udbPath;
  protected File tempDir;

  protected Database db;

  @Before
  public void setup() throws UnderstandException {

    tempDir = Files.createTempDir();
    udbPath = tempDir.getAbsolutePath() + "\\db.udb";

    setProcessTimeOutFieldInUnderstandService();

  }

  private void setProcessTimeOutFieldInUnderstandService() {
    String processTimeOut = "10";

    Field valueField = ReflectionUtils.findField(UnderstandService.class, "processTimeOut");
    ReflectionUtils.makeAccessible(valueField);
    ReflectionUtils.setField(valueField, understandService, processTimeOut);
  }

  @After
  public void tearDown(){

    try {
      FileUtils.delete(tempDir, FileUtils.RECURSIVE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected String getFullResourcePath(String localFilePath){
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(localFilePath).getFile());

    return file.getAbsolutePath();
  }
}
