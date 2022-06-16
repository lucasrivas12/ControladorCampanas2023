package useful_classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import visual_classes.MainPane;

public class SendExecution {
	MainPane main;
	public ArrayList<String> scheduledExecutionList;
	FileHandler fl = new FileHandler();
	FileHandler prevFl = new FileHandler();
	public boolean playSong = false;
	public boolean bellExecution = false;
	String executionToSend;
	String extension;
	Player musicFilePlayer;
	Timer timer;
	Timer executionTimer;
	public boolean okMessage = false;
	//private boolean waitingForArduino = false;
	ExecutionDurationHandler executionDurationHandler;
	public boolean playPrev = true;
	ExecutionHandler executionHandler = new ExecutionHandler();
	osChange os = new osChange();

	public SendExecution(){
		fl.setDirection("sav");
		prevFl.setDirection("files");
		prevFl.setFilename("Ángelus.mp3");
	}
	
	public void setScheduledExecutionList(ArrayList<String> executionList) {
		this.scheduledExecutionList = executionList;
	}
	
	private void playMelodiasByPhone(int melodyNumber) {
		//Modificar cuando se habilite esta función
		FileHandler melodies = new FileHandler();
		melodies.setDirection("files");
		String[] files = melodies.searchFiles(".mp3");
		if(melodyNumber == 0) {
			stopSong();
		}
		else if(melodyNumber <= files.length && !playSong) {
			String melodiaName = files[melodyNumber-1];
			try {
				prepareForExecution(melodiaName);
			} catch (FileNotFoundException | JavaLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("no hay suficientes");
		}
	}
	
	public void compareDateStrings(String actualDateHour) throws FileNotFoundException, JavaLayerException {
		boolean coincide = false;
		boolean dateCoincide;
		boolean dayCoincide;
		boolean hourCoincide;
		//System.out.println("It is in compareDateStrings");

		for(int i=0;i<scheduledExecutionList.size();i++) {
			String[] scheduledParts = scheduledExecutionList.get(i).split(";");
			String[] actualDateHourParts = actualDateHour.split(";");
			boolean isDate = scheduledParts[0].contains("-");
			if(isDate) {
				dateCoincide = scheduledParts[0].equals(actualDateHourParts[1]);
				hourCoincide = scheduledParts[1].equals(actualDateHourParts[2]);
				coincide = dateCoincide && hourCoincide;
			}
			else {
				dayCoincide = scheduledParts[0].contains(actualDateHourParts[0]);
				hourCoincide = scheduledParts[1].equals(actualDateHourParts[2]);
				coincide = dayCoincide && hourCoincide;
			}
			if(coincide) {
				prepareForExecution(scheduledParts[2]);
				if(isDate) deleteExecution(i);
				coincide=false;
				break;
			}
		}
	}
	
	public void  prepareForExecution(String executionName) throws FileNotFoundException, JavaLayerException {
		executionToSend = executionName;
		//("To execute: "+executionToSend);
		setExtension();
		setDirection();
		setFilename();
		startExecution();
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
	}
	
	private void setExtension(){
		extension = executionToSend.split("\\.")[1];
	}

	private void setDirection(){
		fl.setDirection("files");
	}

	private void setFilename(){
		fl.setFilename(executionToSend);
		//System.out.println("Filename: "+executionToSend);
	}

	private void startExecution() throws FileNotFoundException, JavaLayerException{
		main.principalPane.placeBtns(true);
		if(!playSong && !bellExecution)
			if(extension.equals("mp3")){
				//stopSong();
				//if(!playSong)
					playSong();
			} else {
				//System.out.println("Its gonna send to Arduino");
				//if(!bellExecution) {
					bellExecution = true;
					if(main.principalPane.playPrev) {
						playPrevSong();
					} else {
						playExecution();
					}
				//}
			}
	}

	private void deleteExecution(int index) {
		fl.setDirection("sav");
		switch(extension){
			case "mp3":
				fl.setFilename("melodias.int");
				break;
			case "toc":
				fl.setFilename("toques.int");
				break;
			case "sec":
				fl.setFilename("secuencias.int");
				break;
		}
		String sheduledExecution = scheduledExecutionList.get(index);
		String[] fileLines;
		
		fileLines = fl.readFileLine();
		fl.writeFile("",false);
		for(String line: fileLines) {
			if(!line.equals(sheduledExecution)) {
				fl.writeFileln(line,true);
			}
		}
		main.principalPane.fillNameList();
		main.principalPane.reset(false);
		main.principalPane.repaint();
		
		for(String name: scheduledExecutionList){}
			//System.out.println(name);
	}
		
	private void playExecution() {
		String[] fileLines;
		fileLines = fl.readFileLine();
		executionHandler.playExecution(fileLines);
	}
	
	public void playSong() throws FileNotFoundException, JavaLayerException {
		FileInputStream relative = new FileInputStream(fl.getFilePath());
		musicFilePlayer = new Player(relative);
		playSong = true;
		executionHandler.carrillon(true);
	      new Thread() {
	          public void run() {
					try {						
						while(playSong) {
							musicFilePlayer.play(1);
							if(musicFilePlayer.isComplete())
								stopSong();
						}	
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
	          }
	       }.start();      
	}
	
	public void playPrevSong() throws FileNotFoundException, JavaLayerException {
		FileInputStream relative = new FileInputStream(prevFl.getFilePath());
		musicFilePlayer = new Player(relative);
		playSong = true;
		executionHandler.carrillon(true);
	      new Thread() {
	          public void run() {
					try {						
						while(playSong) {
							musicFilePlayer.play(1);
							if(musicFilePlayer.isComplete())
								finishPrevSong();
						}	
					} catch (JavaLayerException e) {
					   e.printStackTrace();
					}
	          }
	       }.start();      
	}
	
	public void buttonStopBellExecution() {
		if(bellExecution) {
			bellExecution = false;
        	main.principalPane.placeBtns(false);
        	executionHandler.stopExecution();
		}
	}
	
	public void stopBellExecution() {
		bellExecution = false; //
    	main.principalPane.placeBtns(false);
    	executionHandler.stopExecution();
        /*if(bellExecution) {
			arduinoVerify();
		}*/
	}
	
	/*public void startTimer(long mili) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	            if(okMessage) {
	            	okMessage = false;
	            	System.out.println("Arduino says ok");
	            }
	            else {
	            	System.out.println("Reseting arduino");
	            	main.resetArduino();
	            }
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = mili;
	    timer.schedule(task, delay);
	}*/
	
	public void clockPulseA() {
		executionHandler.clockPulseA();
	}
	
	public void backlightOn() {
		executionHandler.backlight(true);
	}
	
	public void backlightOff() {
		executionHandler.backlight(false);
	}
	
	public void stopSong() {
		//System.out.println("entré ");
		System.out.print(playSong);
		if(playSong) {
			playSong = false;
			musicFilePlayer.close();
			main.principalPane.placeBtns(false);
			executionHandler.carrillon(false);
		}
	}
	
	public void finishPrevSong() {
		//System.out.println("entré ");
		System.out.print(playSong);
		if(playSong) {
			playSong = false;
			musicFilePlayer.close();
			executionHandler.carrillon(false);
			playExecution();
			//main.principalPane.placeBtns(false);
		}
	}

}