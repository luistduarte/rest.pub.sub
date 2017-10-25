package example.rest.pub.sub;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import util.Runner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

public class SimpleREST extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
	public static void main(String[] args) {
	    Runner.runClusteredExample(SimpleREST.class,
	        new VertxOptions().setEventBusOptions(new EventBusOptions()
	            .setSsl(true)
	            .setKeyStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
	            .setTrustStoreOptions(new JksOptions().setPath("keystore.jks").setPassword("wibble"))
	        )
	    );
	  }

  private Map<String, JsonObject> products = new HashMap<>();

  @Override
  public void start() {

    Router router = Router.router(vertx);
    EventBus eventBus = getVertx().eventBus();

    router.route().handler(BodyHandler.create());
    router.post("/requestpub").handler(this::handleRequestPub);



    vertx.createHttpServer().requestHandler(router::accept).listen(8080);
  }


  private void handleRequestPub(RoutingContext routingContext) {

	JsonObject obj = new JsonObject(routingContext.getBodyAsString().toString());
	String address = obj.getString("address");
	System.out.println("JSON IN:" + routingContext.getBodyAsString() + "\n" + "address:" + address);


    String data = obj.getJsonObject("data").toString();

    routingContext.response().end();
    vertx.eventBus().publish(address, data);
 }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

}
