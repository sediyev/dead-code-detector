package deadcode.variable;

public class SampleDeadVariable2{

	private int unusedIntUsingPublicVariable;
	
	public SampleDeadVariable2(){
		SampleDeadVariable s = new SampleDeadVariable();
		unusedIntUsingPublicVariable = s.usedInAnotherClass;
	}


}