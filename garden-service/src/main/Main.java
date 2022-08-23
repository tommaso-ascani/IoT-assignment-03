package main;

import java.util.concurrent.TimeUnit;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Main {
	
	public static States state; 

	public static void main(String[] args) throws Exception {
		        
        Vertx vertx = Vertx.vertx();
		MQTTAgent agent = new MQTTAgent();
		vertx.deployVerticle(agent);
		VertxOptions vertxOptions = new VertxOptions()
				  .setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(3));
		state = States.IDLE;

	}

}
