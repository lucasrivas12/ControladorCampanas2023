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
	private boolean onExecution = false;
	private boolean onClockPulse = false;
	private boolean onBacklight = false;
	private boolean onGround = false;

	public ExecutionHandler(){
		gpio.initAll();
		//ground(true);
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
		onExecution = false;
		ground(false);
	}

	public void executionHasFinished(){
		onExecution = false;
		ground(false);
	}
	
	public int playExecution(String[] executionLines) {
		onExecution = true;
		executionDuration = 0;
		ground(true);
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
		onClockPulse = true;
		ground(true);
		gpio.setPulse("RA",1000);
		onClockPulse = false;
		ground(false);
	}
	
	public void clockPulseB(int duration) {
		onClockPulse = true;
		ground(true);
		gpio.setPulse("RB",duration);
		onClockPulse = false;
		ground(false);
	}
	
	public void carrillon(boolean state) {
		if(state) {
			onExecution = true;
			ground(true);
			gpio.setHigh("CARRILLON");
		} else {
			onExecution = false;
			ground(false);
			gpio.setLow("CARRILLON");
		}
	}
	
	private void ground(boolean state) {
		if(state) {
			if(!onGround){
				gpio.setHigh("GND");
				onGround = true;
			}
		} else {
			if(!(onExecution || onClockPulse || onBacklight)){
				gpio.setLow("GND");
				onGround = false;
			}
		}
	}
	
	public void backlight(boolean state) {
		if(state) {
			onBacklight = true;
			ground(true);
			gpio.setHigh("BACKLIGHT");
		} else {
			gpio.setLow("BACKLIGHT");
			onBacklight = false;
			ground(false);
		}
	}

}
