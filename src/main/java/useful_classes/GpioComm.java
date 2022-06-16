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
    private static final int PIN_LED = 22; // PIN 15 = BCM 22
	private static int pressCount = 0;
	static DigitalState initialState = DigitalState.HIGH;
	static DigitalState shotdownState = DigitalState.HIGH;
	// crear controlador gpio
	static Context pi4j = Pi4J.newAutoContext();
	Context pi4j2 = Pi4J.newAutoContext();
	static DigitalOutputConfigBuilder config = DigitalOutput.newConfigBuilder(pi4j)
	        .provider("pigpio-digital-output")
	        .shutdown(DigitalState.LOW)
	        .initial(DigitalState.LOW);
	Console console;

	/*static DigitalOutput io4 = pi4j.dout().create(config.address(4));
	static DigitalOutput io5 = pi4j.dout().create(config.address(5));
	static DigitalOutput io6 = pi4j.dout().create(config.address(6));
	static DigitalOutput io12 = pi4j.dout().create(config.address(12));
	static DigitalOutput io17 = pi4j.dout().create(config.address(17));
	static DigitalOutput io18 = pi4j.dout().create(config.address(18));
	static DigitalOutput io22 = pi4j.dout().create(config.address(22));
	static DigitalOutput io23 = pi4j.dout().create(config.address(23));
	static DigitalOutput io24 = pi4j.dout().create(config.address(24));
	static DigitalOutput io25 = pi4j.dout().create(config.address(25));
	static DigitalOutput io27 = pi4j.dout().create(config.address(27));*/
	static DigitalOutput io4;
	static DigitalOutput io5;
	static DigitalOutput io6;
	static DigitalOutput io12;
	static DigitalOutput io17;
	static DigitalOutput io18;
	static DigitalOutput io22;
	static DigitalOutput io23;
	static DigitalOutput io24;
	static DigitalOutput io25;
	static DigitalOutput io27;

	public void init(){
		/*var ledConfig = DigitalOutput.newConfigBuilder(pi4j2)
                .address(PIN_LED)
                .shutdown(shotdownState)
                .initial(initialState)
                .provider("pigpio-digital-output");*/
        //var led = pi4j2.dout().create(ledConfig.address(PIN_LED));

		io4 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(4).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io5 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(5).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io6 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(6).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io12 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(12).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io17 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(17).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io18 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(18).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io22 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(22).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io23 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(23).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io24 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(24).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io25 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(25).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
		io27 = pi4j.create(DigitalOutput.newConfigBuilder(pi4j2)
		.address(27).shutdown(shotdownState)
		.initial(initialState)
		.provider("pigpio-digital-output"));
	}
	private DigitalOutput selectPin(Context pi4j,String sys_pin){
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
	}
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
			return io23;
		case "BC":
			return io22;
		case "BB":
			return io24;
		case "BA":
			return io25;
		case "TB":
			return io27;
		default:
			return null;
		}
	}
	public void GpioTest(){
//		setLow("TA");
 		final int PIN_LED = 22; // PIN 15 = BCM 22
	  	int pressCount = 0;
        //var pi4j = Pi4J.newAutoContext();
		

        // Here we will create I/O interfaces for a (GPIO) digital output
        // and input pin. We define the 'provider' to use PiGpio to control
        // the GPIO.
        /*var ledConfig = DigitalOutput.newConfigBuilder(pi4j2)
                //.id("led")
                //.name("LED Flasher")
                .address(PIN_LED)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output");*/
        //var led = pi4j2.dout().create(ledConfig.address(PIN_LED));
		//var led = pi4j2.create(ledConfig);
		var led = pi4j2.create(DigitalOutput.newConfigBuilder(pi4j2)
				.address(PIN_LED)
                .shutdown(DigitalState.HIGH)
                .initial(DigitalState.LOW)
                .provider("pigpio-digital-output"));
        
		// (This is a utility class to abstract some of the boilerplate stdin/stdout code)
        final var console = new Console();

        // Print program title/header
        console.title("<-- The Pi4J Project -->", "Minimal Example project");
		PrintInfo.printLoadedPlatforms(console, pi4j2);
        PrintInfo.printDefaultPlatform(console, pi4j2);
        PrintInfo.printProviders(console, pi4j2);
		// OPTIONAL: print the registry
        PrintInfo.printRegistry(console, pi4j2);

        while (pressCount < 6) {
			led.toggle();
			console.println("LED "+led.equals(DigitalState.HIGH));
            /*if (led.equals(DigitalState.HIGH)) {
                console.println("LED low");
                io22.toggle();
            } else {
                console.println("LED high");
                io22.toggle();
            }*/
            try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            pressCount++;
        }
		//led.high();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Shutdown pi4j2
        pi4j2.shutdown();
		System.out.println("finishd");
	}
	
	public void setHigh(String sys_pin) {
		selectPin(sys_pin).high();
		System.out.println(sys_pin+" is high");
	}
	
	public void setLow(String sys_pin) {
		selectPin(sys_pin).low();
		System.out.println(sys_pin+" is low");
	}
	
	public void setToggle(String sys_pin) {
		selectPin(sys_pin).toggle();
		System.out.println(sys_pin+" toogle");
	}
	
	public void setPulse(String sys_pin, int miliseconds) {
		selectPin(sys_pin).pulse(miliseconds, TimeUnit.MILLISECONDS);
		System.out.println(sys_pin+" "+miliseconds+" pulse");
	}
	
	public void shutdown() {
		pi4j.shutdown();
	}
}
