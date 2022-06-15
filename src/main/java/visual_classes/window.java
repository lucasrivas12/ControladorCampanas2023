package visual_classes;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import useful_classes.GpioComm;
import useful_classes.osChange;
import java.awt.Point;
/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;*/

public class window extends JFrame {
	osChange os = new osChange();
	MainPane mainPane; 
	private JPanel contentPane;
	private static GpioComm gpio = new GpioComm(); //For test

	/**
	 * Launch the application.
	 */
	public void SetWindow() {
		//System.out.println("hola mundo");
		//System.out.println(this.getClass().getResource("/principal_toques.png"));
		testGpio();
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window frame = new window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}
	
	public static void testGpio() {
		gpio.GpioTest();
		 /*TimerTask repeatedTask = new TimerTask() {
		        public void run() {
		        	//gpio.setPulse("TA",1000);
					gpio.setToggle("TA");
		        }
		 };
		 Timer timer = new Timer("Timer");
		
		long period = 3000L;
		long delay = 1000L;
		timer.scheduleAtFixedRate(repeatedTask, delay, period);*/
	}

	/**
	 * Create the frame.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public window() {
		//Setting size parameters
		//Screen
		Dimension screenSize = os.setDimension();
		//System.out.println(screenSize);
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		
		
		/*String directionCmd = "cd /home/pi/mu_code";
		String openFileCmd = "python3 ledtest.py";
		String tmp;
		String s = new String();
		Process p;

		try{
			p = Runtime.getRuntime().exec(openFileCmd);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while((tmp = br.readLine()) != null)
				System.out.println("[LINE]: " + tmp);
				s += tmp + "\n";
			p.waitFor();
			System.out.println("[EXIT]: "+p.exitValue());
			p.destroy();
		} catch(Exception e){}*/
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 676, 628);
		
		if(!os.ifWindows()){
			setExtendedState(MAXIMIZED_BOTH);
			setUndecorated(true);
			//Cursor hiding
			setCursor(getToolkit().createCustomCursor(
		            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
		            "null"));
		}
		else
			setBounds((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2-screenWidth/2,
					(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2-screenHeight/2,
					screenWidth+16, screenHeight+39);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setLayout(null);
		

		mainPane = new MainPane(this);
		contentPane.add(mainPane);
		mainPane.setBounds(0,0, screenWidth,screenHeight);
		setContentPane(contentPane);
	}
}
