package deadcode.variable;

public class SampleDeadVariable{
  private int unused;

  private int used;

  private int unused2;

  private void use(){
    unused = 1;

    used = 2;

    unused = used;
  }
}