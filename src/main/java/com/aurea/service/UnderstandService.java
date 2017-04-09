package com.aurea.service;

import static com.aurea.util.FileUtils.getUdbPath;

import com.aurea.model.DeadCodeDetection;
import com.aurea.model.UnusedUnderstandEntity;
import com.aurea.model.state.CompletedState;
import com.aurea.model.state.ProcessingState;
import com.aurea.service.finder.DeadCodeFinder;
import com.aurea.service.finder.UnusedFunctionFinder;
import com.aurea.service.finder.UnusedFunctionParameterFinder;
import com.aurea.service.finder.UnusedVariableFinder;
import com.scitools.understand.Database;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;
import java.io.File;
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

  @Value("${command.timeout:150}")
  String processTimeOut;

  Database createAndGetUdbDatabase(File rootFile, DeadCodeDetection deadCodeDetection)
      throws InterruptedException, IOException, TimeoutException, UnderstandException {

    deadCodeDetection.setState(new ProcessingState());
    String udbFilePath = getUdbPath(rootFile);

    createUdbDatabase(udbFilePath, rootFile.getAbsolutePath());

    LOGGER.info("Opening udb database: {}", udbFilePath);
    Database database = Understand.open(udbFilePath);
    LOGGER.info("Opened udb database");

    return database;
  }

  /**
   * Creates udb Understand database.
   *
   * @param udbSource Path where .udb file will be created
   * @param projectSource The location of project source files to be analyzed
   */
  public void createUdbDatabase(String udbSource, String projectSource)
      throws InterruptedException, TimeoutException, IOException {

    LOGGER.info("Creating udb db with udbSource:{}, projectSource: {}", udbSource, projectSource);

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

  /**
   * Finds all dead code occurrences and set end result to deadCodeDetection
   * @param deadCodeDetection object to set dead code occurrences list
   * @param database Understand database
   * @param rootFile Source directory of project to be analyzed
   */
  void findAndSetDeadCodes(DeadCodeDetection deadCodeDetection,
      Database database, File rootFile) {
    List<UnusedUnderstandEntity> deadCodeList = findAllDeadCode(database, rootFile.getAbsolutePath());

    LOGGER.info("Finished processing algorithms: {}", deadCodeDetection.getId());

    deadCodeDetection.setDeadCodeList(deadCodeList);
    deadCodeDetection.setState(new CompletedState());
  }

  /**
   * Executes DeadCodeOccurrence finder algorithms
   * @param db .udb Understand Database
   * @param rootSourceDir source directory of project to be analyzed
   * @return All found dead code occurrences
   */
   private List<UnusedUnderstandEntity> findAllDeadCode(Database db, String rootSourceDir) {

    List<DeadCodeFinder> finderList = Arrays.asList(
        new UnusedFunctionFinder(),
        new UnusedVariableFinder(),
        new UnusedFunctionParameterFinder()
    );

    List<UnusedUnderstandEntity> deadCodeList =  finderList.stream()
        .flatMap(deadCodeFinder -> deadCodeFinder.findAll(db).stream())
        .collect(Collectors.toList());

    return removeTempFolderPrefixFromFileNames(rootSourceDir, deadCodeList);

  }

  private List<UnusedUnderstandEntity> removeTempFolderPrefixFromFileNames(String rootSourceDir,
      List<UnusedUnderstandEntity> deadCodeList) {
    return deadCodeList.stream()
        .map(unusedUnderstandEntity -> makeFileNamePathRelativeToRoot(unusedUnderstandEntity, rootSourceDir))
        .collect(Collectors.toList());
  }

  private UnusedUnderstandEntity makeFileNamePathRelativeToRoot(UnusedUnderstandEntity entity, String rootSourceDir){
    String currentFileName = entity.getFile();

    String newFileName = currentFileName.substring(rootSourceDir.length());
    entity.setFile(newFileName);

    return entity;

  }

}
