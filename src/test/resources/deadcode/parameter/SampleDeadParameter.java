package deadcode.parameter;

public class SampleDeadParameter{

  public void firstFunction(int unusedParameter, int unusedParameter2, int setButNotUsedParameter, int usedParameter, int usedParameter2, int modifiedNotUsed){

    unusedParameter2 = 3;

    modifiedNotUsed++;

    usedParameter = usedParameter2+9;

  }

}
