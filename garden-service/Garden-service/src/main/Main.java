package main;

import io.vertx.core.Vertx;

public class Main {

	public static void main(String[] args) throws Exception {
		        
        Vertx vertx = Vertx.vertx();
		MQTTAgent agent = new MQTTAgent();
		vertx.deployVerticle(agent);
		
		

	}

}
