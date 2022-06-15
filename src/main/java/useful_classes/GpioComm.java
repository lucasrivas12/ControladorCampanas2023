package useful_classes;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
//import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
//import com.pi4j.io.gpio.digital.PullResistance;
//import com.pi4j.util.Console;


import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.PullResistance;
import com.pi4j.util.Console;

import java.util.concurrent.TimeUnit;

//import com.pi4j.*;

public class GpioComm {
    /*private static final int PIN_LED = 22; // PIN 15 = BCM 22
	private static int pressCount = 0;
	// crear controlador gpio
	Context pi4j = Pi4J.newAutoContext();
	DigitalOutputConfigBuilder config = DigitalOutput.newConfigBuilder(pi4j)
	        .provider("pigpio-digital-output")
	        .shutdown(DigitalState.LOW)
	        .initial(DigitalState.LOW);

	DigitalOutput io4 = pi4j.dout().create(config.address(4));
	DigitalOutput io5 = pi4j.dout().create(config.address(5));
	DigitalOutput io6 = pi4j.dout().create(config.address(6));
	DigitalOutput io12 = pi4j.dout().create(config.address(12));
	DigitalOutput io17 = pi4j.dout().create(config.address(17));
	DigitalOutput io18 = pi4j.dout().create(config.address(18));
	DigitalOutput io22 = pi4j.dout().create(config.address(22));
	DigitalOutput io23 = pi4j.dout().create(config.address(23));
	DigitalOutput io24 = pi4j.dout().create(config.address(24));
	DigitalOutput io25 = pi4j.dout().create(config.address(25));
	DigitalOutput io27 = pi4j.dout().create(config.address(27));
	
	private DigitalOutput selectPin(String sys_pin){
		switch(sys_pin) {
		case "BACKLIGHT":
			return io4;
		case "RB":
			return io5;
		case "GND":
			return io6;
		case "CARRILLON":
			return io12;
		case "RA":
			return io17;
		case "TC":
			return io18;
		case "TA":
			return io22;
		case "BC":
			return io23;
		case "BB":
			return io24;
		case "BA":
			return io25;
		case "TB":
			return io27;
		default:
			return null;
		}
	}*/
	public void GpioTest(){
//		setLow("TA");
 		final int PIN_LED = 22; // PIN 15 = BCM 22
	  	int pressCount = 0;
        var pi4j = Pi4J.newAutoContext();
		

        // Here we will create I/O interfaces for a (GPIO) digital output
        // and input pin. We define the 'provider' to use PiGpio to control
        // the GPIO.
        var ledConfig = DigitalOutput.newConfigBuilder(pi4j)
                //.id("led")
                //.name("LED Flasher")
                .address(PIN_LED)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");
        var led = pi4j.create(ledConfig);

        
		// (This is a utility class to abstract some of the boilerplate stdin/stdout code)
        final var console = new Console();

        // Print program title/header
        console.title("<-- The Pi4J Project -->", "Minimal Example project");
		PrintInfo.printLoadedPlatforms(console, pi4j);
        PrintInfo.printDefaultPlatform(console, pi4j);
        PrintInfo.printProviders(console, pi4j);
		// OPTIONAL: print the registry
        PrintInfo.printRegistry(console, pi4j);

        while (pressCount < 6) {
            if (led.equals(DigitalState.HIGH)) {
                console.println("LED low");
                led.low();
            } else {
                console.println("LED high");
                led.high();
            }
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            pressCount++;
        }

        // Shutdown Pi4J
        pi4j.shutdown();
	}
	
	public void setHigh(String sys_pin) {
		//selectPin(sys_pin).high();
		System.out.println(sys_pin+" is high");
	}
	
	public void setLow(String sys_pin) {
		//selectPin(sys_pin).low();
		System.out.println(sys_pin+" is low");
	}
	
	public void setToggle(String sys_pin) {
		//selectPin(sys_pin).toggle();
		System.out.println(sys_pin+" toogle");
	}
	
	public void setPulse(String sys_pin, int miliseconds) {
		//selectPin(sys_pin).pulse(miliseconds, TimeUnit.MILLISECONDS);
		System.out.println(sys_pin+" "+miliseconds+" pulse");
	}
	
	public void shutdown() {
		//pi4j.shutdown();
	}
}
