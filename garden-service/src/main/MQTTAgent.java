package main;

import java.sql.SQLException;
import java.util.Random;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {
	
	String serialPortName;
	ExtendedSerialCommChannel channel;
	
	public MQTTAgent() throws Exception {
		this.serialPortName = "COM3"; // It must be changed!
		/* setting up the channel, with server for the emu*/
        System.out.print("Creating the serial comm channel with IP server ...");
        this.channel = new ExtendedSerialCommChannel(serialPortName, 9600, 8080);
        System.out.println("Ready.");
	}

	@Override
	public void start() {
		System.out.println("cacca");
		MqttClient client = MqttClient.create(vertx);

		client.connect(1883, "broker.mqtt-dashboard.com", c -> {
			System.out.println("cacca");

			log("connected");
			
			log("subscribing...");
			client.publishHandler(s -> {
//				String msgReceivedAndroid = "";
//				try {
//					msgReceivedAndroid = this.channel.receiveMsg();
//					System.out.println(msgReceivedAndroid);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				String msgReceivedEsp = s.payload().toString();
				System.out.println(msgReceivedEsp);
				System.out.println("There are new message in topic: " + s.topicName());
				System.out.println("Content(as string) of the message: " + msgReceivedEsp);
			  	switch(msgReceivedEsp) {
			  		case "AlarmOn": {
			  			System.out.println("Alarm");
			  			Main.state = States.ALARM;
			  			break;
			  		}
			  		case "IrSpeed1": {
			  			//Setto la velocità di irrigazione a x
			  			System.out.println("IrSpeed1");
			  			this.channel.sendMsg("I1");
			  			break;
			  		}
			  		case "IrSpeed2": {
			  			System.out.println("IrSpeed2");
			  			this.channel.sendMsg("I2");
			  			break;
			  		}
			  		case "IrSpeed3": {
			  			System.out.println("IrSpeed3");
			  			this.channel.sendMsg("I3");
			  			break;
			  		}
			  		case "IrSpeed4": {
			  			System.out.println("IrSpeed4");
			  			this.channel.sendMsg("I4");
			  			break;
			  		}
			  		case "Led1234On": {
			  			//Accendo i primi due led e imposto a ?piacere? gli altri led regolabili
			  			System.out.println("Led1234On");
			  			try {
							this.channel.DBU.changeValue("led_1", 1);
							this.channel.DBU.changeValue("led_2", 1);
							Random random = new Random();
							int rand = 0;
							while (true){
							    rand = random.nextInt(5);
							    if(rand !=0) break;
							}
				  			this.channel.DBU.changeValue("led_3", rand);
				  			random = new Random();
							rand = 0;
							while (true){
							    rand = random.nextInt(5);
							    if(rand !=0) break;
							}
				  			this.channel.DBU.changeValue("led_4", rand);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			  			this.channel.sendMsgToArduino();
			  			break;
			  		}
			  		case "Led1234Off": {
			  			try {
			  				System.out.println("Led off");
							this.channel.DBU.changeValue("led_1", 0);
							this.channel.DBU.changeValue("led_2", 0);
				  			this.channel.DBU.changeValue("led_3", 0);
				  			this.channel.DBU.changeValue("led_3", 0);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			  			this.channel.sendMsgToArduino();
			  		}
			  		case "IrOn": {
			  			//Faccio partire l'irrigazione
			  			System.out.println("IrOn");
			  			this.channel.sendMsg("IO");
			  			break;
			  		}
			  	}
			  	
//			  	switch(msgReceivedAndroid) {
//			  		case "AlarmOff" : {
//			  			System.out.println("AlarmOff");
//			  			Main.state = States.MANUAL;
//			  			client.publish("pc to esp",
//			  				  Buffer.buffer("AlarmOff"),
//			  				  MqttQoS.AT_LEAST_ONCE,
//			  				  false,
//			  				  false);
//			  		}
//			  	}
			})
			.subscribe("esp to pc", 1);		

//			log("publishing a msg");
//			client.publish("pc to esp",
//				  Buffer.buffer("hello"),
//				  MqttQoS.AT_LEAST_ONCE,
//				  false,
//				  false);
		});
	}
	

	private void log(String msg) {
		System.out.println("[DATA SERVICE] "+msg);
	}

}