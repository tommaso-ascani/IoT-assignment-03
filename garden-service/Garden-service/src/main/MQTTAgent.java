package main;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {
	
	String serialPortName;
	CommChannel channel;
	
	public MQTTAgent() throws Exception {
		this.serialPortName = "COM3"; // It must be changed!
		/* setting up the channel, with server for the emu*/
        System.out.print("Creating the serial comm channel with IP server ...");
        this.channel = new ExtendedSerialCommChannel(serialPortName, 9600, 8080);
        System.out.println("Ready.");
	}

	@Override
	public void start() {		
		MqttClient client = MqttClient.create(vertx);

		client.connect(1883, "broker.mqtt-dashboard.com", c -> {

			log("connected");
			
			log("subscribing...");
			client.publishHandler(s -> {
				String msgReceived = s.payload().toString();
				System.out.println("There are new message in topic: " + s.topicName());
				System.out.println("Content(as string) of the message: " + msgReceived);
			  	switch(msgReceived) {
			  		case "Ir?": {
			  			System.out.println("Ir?");
			  			String msgToSend = "IrOff";
			  			if(/*IrrigationOn*/true) {
			  				msgToSend = "IrOn";
			  			}
			  			client.publish("pc to esp",
								Buffer.buffer(msgToSend),
								MqttQoS.AT_LEAST_ONCE,
								false,
								false);
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
			  			this.channel.sendMsg("LO");
			  			break;
			  		}
			  		case "IrOn": {
			  			//Faccio partire l'irrigazione
			  			System.out.println("IrOn");
			  			this.channel.sendMsg("IO");
			  			break;
			  		}
			  	}
			})
			.subscribe("esp to pc", 2);		

			log("publishing a msg");
			client.publish("pc to esp",
				  Buffer.buffer("hello"),
				  MqttQoS.AT_LEAST_ONCE,
				  false,
				  false);
		});
	}
	

	private void log(String msg) {
		System.out.println("[DATA SERVICE] "+msg);
	}

}