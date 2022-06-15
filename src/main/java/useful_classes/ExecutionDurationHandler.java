package useful_classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExecutionDurationHandler {
	private String[] exectutionLines;
	private int executionMili = 0;
	private Pattern toquePattern = Pattern.compile("[ABC](\\d\\d\\d\\d\\d\\d)");
	private Pattern bandeoPattern = Pattern.compile("[ABC](\\d{6})#(\\d{6})");
	
	void setExecutionLines(String[] exectutionLines){
		this.exectutionLines = exectutionLines;
	}
	
	int getDuration() {
		executionMili = 0;
		if(exectutionLines[0].contains("t")) 
			setDurationOnToques();
		else if(exectutionLines[0].contains("b")) 
			setDurationOnBandeos();
		return executionMili;
	}
	
	void setDurationOnToques() {
		for(int i=1;i<exectutionLines.length-1;i++) {
			Matcher match = toquePattern.matcher(exectutionLines[i]);
			match.find();
			//System.out.println(match.group(1));
			int mili = Integer.parseInt(match.group(1));
			//System.out.println(mili);
			executionMili += mili;
		}
	}
	
	void setDurationOnBandeos() {
		int aux = 0;
		for(int i=1;i<exectutionLines.length-1;i++) {
			Matcher match = bandeoPattern.matcher(exectutionLines[i]);
			match.find();
			int miliWait = Integer.parseInt(match.group(1));
			int miliDuration = Integer.parseInt(match.group(2));
			aux += miliWait;
			executionMili = Math.max(executionMili,aux + miliDuration);
			//System.out.println(executionMili);
		}
	}
	
}
