package com.aurea.service;

import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.service.lookup.DeadCodeFinder;
import com.aurea.service.lookup.UnusedFunctionFinder;
import com.aurea.service.lookup.UnusedFunctionParameterFinder;
import com.aurea.service.lookup.UnusedVariableFinder;
import com.scitools.understand.Database;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.ProcessExecutor;

@Service
public class UnderstandService {

  private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Value("${command.timeou}")
  String processTimeOut;

  /**
   * Creates udb Understand database.
   *
   * @param udbSource Path where .udb file will be created
   * @param projectSource The location of project source files to be analyzed
   */
  public void createUdb(String udbSource, String projectSource)
      throws InterruptedException, TimeoutException, IOException {

    LOGGER.info("Creating udb db with udbSource:{}, projectSourcec: {}", udbSource, projectSource);

    executeProcess(udbSource, projectSource);

  }

  /**
   * * Executes command in the format:
   * und create -db db.udb -languages Java add . analyze
   */
  private void executeProcess(String udbFilePath, String projectDirPath)
      throws IOException, InterruptedException, TimeoutException {
    new ProcessExecutor()
        .command("und",
            "create", "-db", udbFilePath,
            "-languages", "Java",
            "add", projectDirPath, "analyze")
        .timeout(Long.valueOf(processTimeOut), TimeUnit.SECONDS)
        .exitValue(0)
        .execute();
  }


   List<UnusedUnderstandEntity> findAllDeadCode(Database db, String rootSourceDir) {

    List<DeadCodeFinder> finderList = Arrays.asList(
        new UnusedFunctionFinder(),
        new UnusedVariableFinder(),
        new UnusedFunctionParameterFinder()
    );

    List<UnusedUnderstandEntity> deadCodeList =  finderList.stream()
        .flatMap(deadCodeFinder -> deadCodeFinder.findAll(db).stream())
        .collect(Collectors.toList());

    return deadCodeList.stream()
        .map(unusedUnderstandEntity -> truncateFile(unusedUnderstandEntity, rootSourceDir))
        .collect(Collectors.toList());

  }

  private UnusedUnderstandEntity truncateFile(UnusedUnderstandEntity entity, String rootSourceDir){
    String currentFileName = entity.getFile();

    String newFileName = currentFileName.substring(rootSourceDir.length());
    entity.setFile(newFileName);

    return entity;

  }


}
