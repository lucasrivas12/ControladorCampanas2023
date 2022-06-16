package visual_classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import javazoom.jl.decoder.JavaLayerException;
import useful_classes.SendExecution;
import useful_classes.osChange;


public class DateAndHour extends JPanel {
	//Other atributes
	JLabel fecha;
	JLabel hora;
	Timer timer;
	Timer arduinoDelayTimer;
	int time = 20*1 + 60*3+60*60*1;
	String dateForCompare = "";
	String texto_hora;
	int texto_dia;
	String texto_mes;
	String texto_ano;
	String text_date;
	String mes[] = {"enero", "febrero", "marzo", "abril", 
			"mayo", "junio", "julio", "agosto", "septiembre", 
			"octubre", "noviembre", "diciembre"};
	Date date = new Date();
	boolean minuteChange = false;
	boolean testOnce = true;
	boolean backlight = false;
	osChange os = new osChange();
	SendExecution sendExecution = new SendExecution();
	MainPane main;
	
	/**
	 * Create the panel.
	 */
	public DateAndHour() {
		Dimension screenSize = os.setDimension();
		int screenWidth = (int)screenSize.getWidth();
		//int screenHeight = (int)screenSize.getHeight();
		
		//Texto fecha y hora
		int date_panel_w = 700;
		int label_h = 35;
		int date_gap = 10;
		int date_panel_h = label_h * 2;
		
		setOpaque(false);
		setBorder(null);
		setBounds(screenWidth - date_panel_w - date_gap, date_gap, date_panel_w, date_panel_h);
		
		getDate();
		String date = text_date+", "+texto_dia+" de "+mes[Integer.parseInt(texto_mes)-1];
		fecha = new JLabel(date);
		fecha.setForeground(Color.WHITE);
		fecha.setHorizontalAlignment(SwingConstants.RIGHT);
		fecha.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 30));
		fecha.setBounds(0, 0, date_panel_w, label_h);
		
		hora = new JLabel(texto_hora);
		hora.setForeground(Color.WHITE);
		hora.setHorizontalAlignment(SwingConstants.RIGHT);
		hora.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 30));
		hora.setBounds(0, label_h, date_panel_w, label_h);
		setLayout(null);
		add(fecha);
		add(hora);
	}
	
	public void setMainPane(MainPane main) {
		this.main = main;
		//startTimer(time);
		timeDateUpdate();
		//resetArduinoUpdate();

		//this.main.addListenerToUpdate(this);
	}
	
	private void getDate() {
		Date date = new Date();
		//date.setHours(18);
		//date.setMinutes(0);
		DateFormat secondFormat = new SimpleDateFormat("ss");
		
		//Hora
		DateFormat hourFormat = new SimpleDateFormat("hh:mm aa");
		//String test = hourFormat.format(date);
		/*for(int i=0;i<test.length();i++){
			System.out.println(i+": "+test.charAt(i)+", "+(int)test.charAt(i));
		}*/
		char replaceDigit = os.ifWindows()? (char)160:(char)32;
		String replacedText = hourFormat.format(date).replace(String.valueOf(replaceDigit),"");
		replacedText = os.ifWindows()? replacedText:replacedText.substring(0,5)+" "+replacedText.substring(5);
		minuteChange = !replacedText.equals(texto_hora);
		texto_hora = replacedText;
		
		//Da
		DateFormat dayFormat = new SimpleDateFormat("d");
		texto_dia = Integer.parseInt(dayFormat.format(date));
		
		//Mes
		DateFormat dateFormat = new SimpleDateFormat("M");
		texto_mes = dateFormat.format(date);
		//System.out.println("dia: "+dayFormat.format(date)+", month: "+dateFormat.format(date));
		//Ao
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		texto_ano = yearFormat.format(date);
		
		//Da semana
		DateFormat dayOfWeekFormat = new SimpleDateFormat("u");
		int weekCorrect = Integer.parseInt(dayOfWeekFormat.format(date))+1;
		weekCorrect = weekCorrect==8? 1:weekCorrect;
		text_date = diaSemana(weekCorrect);
		
		DateFormat forCompareHourFormat = new SimpleDateFormat("hh:mmaa");
		DateFormat forCompareDateFormat = new SimpleDateFormat("d-M-yyyy");
		String hourCompare = forCompareHourFormat.format(date).replace(String.valueOf((char)replaceDigit),"");
		String dateCompare = forCompareDateFormat.format(date);
		
		dateForCompare = weekCorrect+";"+dateCompare+";"+hourCompare;
		//System.out.println(dateForCompare);
	}
	
	/*public void startTimer(int seg) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	            main.save_screen_on = true;
				main.menuNavegation.screenSaver();
	        	System.out.println("Timer finished");
	        }
	    };
	    timer = new Timer("Timer");
	    
	    long delay = seg*1000L;
	    timer.schedule(task, delay);
	}*/
	
	/*public void arduinoClockPulseDaley(int seg) {
	    TimerTask task = new TimerTask() {
	        public void run() {
	        	//Clock pulse
        		main.sendExecution.clockPulseA();
        		
        		if(texto_hora.equals("06:05 p.m.")) {
        			main.sendExecution.backlightOn();
        		}
        		else if(texto_hora.equals("06:05 a.m.")) {
        			main.sendExecution.backlightOff();
        		}
	        }
	    };
	    arduinoDelayTimer = new Timer("Timer");
	    
	    long delay = seg*1000L;
	    arduinoDelayTimer.schedule(task, delay);
	}*/
	
	public void update() {
		//System.out.println("update");
		/*timer.cancel();
		timer.purge();
		startTimer(time);*/
	}
	
	private String diaSemana (int nD)
    {
		String letraD = "";		
        switch (nD){
            case 1: letraD = "Domingo";
                break;
            case 2: letraD = "Lunes";
                break;
            case 3: letraD = "Martes";
                break;
            case 4: letraD = "Miércoles";
                break;
            case 5: letraD = "Jueves";
                break;
            case 6: letraD = "Viernes";
                break;
            case 7: letraD = "Sábado";
                break;
        }
        return letraD;
    }
	
	public void timeDateUpdate() {
		Date date = new Date();
		DateFormat secondFormat = new SimpleDateFormat("ss");
		String firstSeconds = secondFormat.format(date);
		String seconds = firstSeconds;
		
		while(seconds.equals(firstSeconds)) {
			Date date1 = new Date();
			seconds = secondFormat.format(date1);
		}
		
	    TimerTask repeatedTask = new TimerTask() {
	        public void run() {    	
	        	getDate();
	        	if(minuteChange) {
	        		//System.out.println("it do a minuteChange: "+minuteChange);
	        		minuteChange=false;
					try {
						main.sendExecution.compareDateStrings(dateForCompare);
					} catch (FileNotFoundException | JavaLayerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//To handle to clock and backlight by arduino
					//arduinoClockPulseDaley(30);
					main.sendExecution.clockPulseA();
	        		
	        		if(texto_hora.equals("06:00 p.m.") && !backlight) {
	        			main.sendExecution.backlightOn();
	        			backlight = !backlight;
	        		}
	        		else if(texto_hora.equals("06:00 a.m.") && backlight) {
	        			main.sendExecution.backlightOff();
	        			backlight = !backlight;
	        		}
				
	        	}
	        	String date = text_date+", "+texto_dia+" de "+mes[Integer.parseInt(texto_mes)-1];
	        	if(date.length() > 24) {
	        		date = texto_dia+" de "+mes[Integer.parseInt(texto_mes)-1];
	        	}
	    		fecha.setText(date); 
	    		hora.setText(texto_hora);
	        }
	    };
	    Timer timer = new Timer("Timer");
	    
	    long delay = 0L;
	    long period = 1000L;
	    timer.scheduleAtFixedRate(repeatedTask, delay, period);
	}
	
	/*public void resetArduinoUpdate() {
		 TimerTask repeatedTask = new TimerTask() {
		        public void run() {
		        	main.resetArduino();
		        }
		 };
		 Timer timer = new Timer("Timer");
		
		long delay = 0L;
		long second = 1000L;
		long hour = second * 3600;
		long day = hour * 24;
		long period = day * 1;
		timer.scheduleAtFixedRate(repeatedTask, delay, period);
	}*/

}