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

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Handler;
import java.util.logging.Level;

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
	ExecutionDurationHandler executionDurationHandler;
	public boolean playPrev = true;
	ExecutionHandler executionHandler = new ExecutionHandler();
	osChange os = new osChange();
	Logger logger = Logger.getLogger("MyLog");  
    Handler fh;  

	public SendExecution(){
		try {
			fl.setDirection("sav");
			prevFl.setDirection("files");
			prevFl.setFilename("Ángelus.mp3");
		} catch (Exception e) {
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
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
				LoggingTester("ERROR: playMelodiasByPhone" + String.valueOf( e));
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
		try {
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
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
	}
	
	public void  prepareForExecution(String executionName) throws FileNotFoundException, JavaLayerException {
		executionToSend = executionName;
		//("To execute: "+executionToSend);
		try {
			setExtension();
			setDirection();
			setFilename();
			startExecution();
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
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
		try {
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
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
	}

	private void deleteExecution(int index) {
		fl.setDirection("sav");
		try {
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
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
	}
		
	private void playExecution() {
		String[] fileLines;
		int duration;
		try {
			fileLines = fl.readFileLine();
			duration = executionHandler.playExecution(fileLines);
			executionFinished(duration);
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
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
					   LoggingTester("ERROR:" + String.valueOf( e));
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
					   LoggingTester("ERROR:" + String.valueOf( e));
					}
	          }
	       }.start();      
	}
	
	public void stopBellExecution() {
		try {
			if(bellExecution) {
				bellExecution = false;
				main.principalPane.placeBtns(false);
				executionHandler.stopExecution();
			}
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
	}

	public void executionFinished(){
		
		bellExecution = false;
		try {
			
			main.principalPane.placeBtns(false);
			executionHandler.executionHasFinished();
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
	}
	
	public void executionFinished(long mili) {
		try {
			TimerTask task = new TimerTask() {
				public void run() {
					bellExecution = false;
					main.principalPane.placeBtns(false);
					executionHandler.executionHasFinished();
				}
			};
			timer = new Timer("Timer");
			
			long delay = mili;
			timer.schedule(task, delay);
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
	    
	}
	
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
		try {
			if(playSong) {
				playSong = false;
				musicFilePlayer.close();
				main.principalPane.placeBtns(false);
				executionHandler.carrillon(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
	}
	
	public void finishPrevSong() {
		//System.out.println("entré ");
		System.out.print(playSong);
		try {
			if(playSong) {
				playSong = false;
				musicFilePlayer.close();
				executionHandler.carrillon(false);
				playExecution();
				//main.principalPane.placeBtns(false);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LoggingTester("ERROR:" + String.valueOf( e));
		}
		
	}

	public void LoggingTester(String message) {
		try {
			// This block configure the logger with handler and formatter  
			fh = new java.util.logging.FileHandler("MyLogFile.log");  
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);  
	
			// the following statement is used to log any messages  
			logger.info(message);  
		} catch (Exception e) {
			e.printStackTrace();  
			logger.info(e.toString());  
		}
	}

}