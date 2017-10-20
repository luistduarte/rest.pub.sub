package example.rest.pub.sub;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.net.JksOptions;
import util.Runner;


public class Receiver extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
	public static void main(String[] args) {
	    Runner.runClusteredExample(Receiver.class, new VertxOptions().setEventBusOptions(new EventBusOptions()
	            .setSsl(true)
	            .setKeyStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
	            .setTrustStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
	        )
	    );
	  }


  @Override
  public void start() throws Exception {

    EventBus eb = vertx.eventBus();

    eb.consumer("heart-rate", message -> System.out.println("Heart-Rate DP: " + message.body()));

    System.out.println("Ready!");
  }
}
