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
	//private static GpioComm gpio = new GpioComm(); //For test

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//System.out.println(gpio.PIN_LED);
		//gpio.PIN_LED = 56;

		/*try {
			
			testGpio();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window frame = new window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*public static void testGpio() throws InterruptedException {
		
		TimerTask task = new TimerTask() {
		        public void run() {
		        	//gpio.setPulse("TA",1000);
					GpioComm g2 = new GpioComm();
					g2.initPin(23);
					g2.setHigh("TA");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					g2.setLow("TA");
					//g2.shutdown();
		        }
		 };
		 Timer timer = new Timer("Timer");
		
		long period = 2000L;
		long delay = 6000L;
		timer.schedule(task, delay);

		gpio.initPin(24);
		for(int i=0;i<5;i++){
			gpio.setPulse("BB",500);
			Thread.sleep(3000);
		}
		gpio.shutdown();
		
	}*/

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
