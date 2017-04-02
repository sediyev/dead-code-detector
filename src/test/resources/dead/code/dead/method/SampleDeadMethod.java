package dead.code.dead.method;

public class SampleDeadMethod {
  public SampleDeadMethod(){
    usedInsideConstuctor();
  }

  private void usedInsideMethod(){

  }

  private void usedInsideConstuctor(){

  }

  private void unusedMethod(){
    usedInsideMethod();
  }

  private int unusedMethod2(int number){
    return number;
  }

}
