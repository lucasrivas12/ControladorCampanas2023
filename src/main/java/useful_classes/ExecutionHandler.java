package useful_classes;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExecutionHandler {
	private GpioComm gpio = new GpioComm();
	private String[] executionLines;
	private Pattern toquePattern = Pattern.compile("([ABC])(\\d{6})");
	private Pattern bandeoPattern = Pattern.compile("([ABC])(\\d{6})#(\\d{6})");
	private boolean isToques;
	private ArrayList<Timer> timer = new ArrayList();
	private int executionDuration;
	private int extraDuration = 200;

	public ExecutionHandler(){
		gpio.initAll();
		ground(true);
	}
	private void setExecutionLines(String[] executionLines){
		this.executionLines = executionLines;
	}
	
	private void defineType() {
		isToques = executionLines[0].equals("t");
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
		if (isToques){
			gpio.setLow("TA");
			gpio.setLow("TB");
			gpio.setLow("TC");
		} else {
			gpio.setLow("BA");
			gpio.setLow("BB");
			gpio.setLow("BC");
		}
		
	}
	
	public int playExecution(String[] executionLines) {
		executionDuration = 0;
		setExecutionLines(executionLines);
		defineType();
		if(isToques)
			playToques();
		else
			playBandeo();
		return executionDuration+extraDuration;
	}

	private void playToques(){
		char executionType = 'T';
		int delay = 0;
		for(int i=1;i<executionLines.length-1;i++) {
			Matcher match = toquePattern.matcher(executionLines[i]);
			match.find();
			int mili = Integer.parseInt(match.group(2));
			delay += mili;
			String note = executionType + match.group(1);
			newNote(note,delay);
		}
		executionDuration = delay + 1000;
	}

	private void playBandeo(){
		char executionType = 'B';
		int delay = 0;
		for(int i=1;i<executionLines.length-1;i++) {
			Matcher match = bandeoPattern.matcher(executionLines[i]);
			match.find();
			int mili = Integer.parseInt(match.group(2));
			int duration = Integer.parseInt(match.group(3));
			delay += mili;
			String note = executionType + match.group(1);
			newNote(note,delay,duration);
			executionDuration = Math.max(executionDuration,delay + duration);
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
