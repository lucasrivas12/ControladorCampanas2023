package useful_classes;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExecutionHandler {
	private ArrayList<String> fileData = new ArrayList();
	private GpioComm gpio = new GpioComm();
	private String[] executionLines;
	private Pattern toquePattern = Pattern.compile("([ABC])(\\d{6})");
	private Pattern bandeoPattern = Pattern.compile("([ABC])(\\d{6})#(\\d{6})");
	boolean isToques;
	private Thread exe_thread;
	private ArrayList<Timer> timer = new ArrayList();
	private void setExecutionLines(String[] executionLines){
		this.executionLines = executionLines;
	}
	
	private void defineType() {
		isToques = executionLines[0].equals("t");
	}
	
	private void playingToques() {
		
	}
	
	private void newNote(String note,long delay,int duration) {
		TimerTask task = new TimerTask() {
			public void run() {
				gpio.setPulse(note,duration);
			}
		};
		timer.add(new Timer("Timer"));
	    timer.get(timer.size()-1).schedule(task, delay);
	}
	
	private void newNote(String note,long delay) {
		int duration = 1000;
		TimerTask task = new TimerTask() {
			public void run() {
				gpio.setPulse(note,duration);
			}
		};
		timer.add(new Timer("Timer"));
	    timer.get(timer.size()-1).schedule(task, delay);;
	}
	
	public void stopExecution() {
		if(timer.size() > 0) {
			timer.forEach((t) -> t.cancel());
			timer.forEach((t) -> t.purge());
		}
	}
	
	public void playExecution(String[] executionLines) {
		setExecutionLines(executionLines);
		defineType();
		int delay = 0;
		char executionType = isToques? 'T':'B';
		for(int i=1;i<executionLines.length-1;i++) {
			Matcher match = toquePattern.matcher(executionLines[i]);
			match.find();
			int mili = Integer.parseInt(match.group(2));
			delay += mili;
			String note = executionType + match.group(1);
			newNote(note,delay);
		}
	}
	
	public void clockPulseA() {
		gpio.setPulse("RA",1000);
	}
	
	public void clockPulseB(int duration) {
		gpio.setPulse("RB",duration);
	}
	
	public void carrillon(boolean state) {
		if(state) {
			gpio.setHigh("CARRILLON");
		} else {
			gpio.setLow("CARRILLON");
		}
	}
	
	public void ground(boolean state) {
		if(state) {
			gpio.setHigh("GND");
		} else {
			gpio.setLow("GND");
		}
	}
	
	public void backlight(boolean state) {
		if(state) {
			gpio.setHigh("BACKLIGHT");
		} else {
			gpio.setLow("BACKLIGHT");
		}
	}

}
