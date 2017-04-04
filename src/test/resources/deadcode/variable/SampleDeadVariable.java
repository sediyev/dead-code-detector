package deadcode.variable;

public class SampleDeadVariable{
  private int unused;

  private int used;

  private int unused2;

  public int usedInAnotherClass;

  private void use(){

    int unused 3;

    unused = 1;

    used = 2;

    unused = used;
  }
}