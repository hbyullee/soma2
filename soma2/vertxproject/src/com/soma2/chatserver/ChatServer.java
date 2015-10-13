package com.soma2.chatserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChatServer extends AbstractVerticle {
	private List<String> handlerIds;
	final Pattern chatUrlPattern = Pattern.compile("/chat/(\\w+)");
	@Override
	public void start() throws Exception {

		super.start();
		handlerIds = new ArrayList<String>();
		EventBus eventBus = vertx.eventBus();
	    
		eventBus.consumer("Test.Login", (Message<JsonObject> message) -> {

			JsonObject param = message.body();
			System.out.println(param.getString("METHOD") + " Start!!!");
			message.reply(param.getString("METHOD") + " End");

		});

		Router router = Router.router(vertx);
		Route route = router.route("/:METHOD");

		route.handler(routingContext -> {

			HttpServerRequest request = routingContext.request();
			MultiMap params = request.params();

			String method = params.get("METHOD");
			System.out.println(method);

			request.endHandler(empty -> {

				JsonObject param = new JsonObject();
				params.forEach(entry -> param.put(entry.getKey(), entry.getValue()));
				if("login".equals(method)&&!vertx.sharedData().getLocalMap("com.soma2.chatserver").isEmpty()){
					eventBus.send("Test." + method, param, (AsyncResult<Message<String>> result) -> {
	
						if (result.succeeded()) {
	
//							String returnValue = result.result().body();
	
							request.response().putHeader("content-type", "application/json");
							request.response().putHeader("Access-Control-Allow-Origin", "*" );
							request.response().end((String)vertx.sharedData().getLocalMap("com.soma2.chatserver").get("data"));
	
						} else {
	
							request.response().putHeader("content-type", "application/json");
							request.response().putHeader("Access-Control-Allow-Origin", "*" );
							request.response().end((String)vertx.sharedData().getLocalMap("com.soma2.chatserver").get("data"));
							result.cause().printStackTrace();
	
						}
	
					});
				}
			});

		});

		HttpServerOptions httpServerOptions = new HttpServerOptions();
		httpServerOptions.setCompressionSupported(true);

		vertx.createHttpServer(httpServerOptions)
				.requestHandler(router::accept)
				.listen(8070);
		
		vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {
			@Override
			public void handle(final ServerWebSocket ws) {
			
				final String id = ws.textHandlerID();
				handlerIds.add(id);
				ws.closeHandler(new Handler<Void>() {
					@Override
					public void handle(final Void event) {
						
						Iterator<String> it = handlerIds.iterator();
						while(it.hasNext()) {
							if(id.equals(it.next())){
								it.remove();
								break;
							}
						}	
					}
				});
		 
				ws.handler(new Handler<Buffer>() {
					@Override
					public void handle(final Buffer data) {
				
						ObjectMapper m = new ObjectMapper();
						try {
							JsonNode rootNode = m.readTree(data.toString());
//							((ObjectNode) rootNode).put("received", new Date().toString());
							String jsonOutput = m.writeValueAsString(rootNode);
							JsonArray jsonArray;
							if( vertx.sharedData().getLocalMap("com.soma2.chatserver").isEmpty()){
								 jsonArray = new JsonArray();
							}else{
								 jsonArray =  new JsonArray( (String)vertx.sharedData().getLocalMap("com.soma2.chatserver").get("data"));
							}
								jsonArray.add(jsonOutput);
								vertx.sharedData().getLocalMap("com.soma2.chatserver").put("data", jsonArray.toString());
								System.out.println(vertx.sharedData().getLocalMap("com.soma2.chatserver").get("data"));
//							for (Object chatter : handlerIds) {
//								eventBus.send((String) chatter, jsonOutput);
//							}
								eventBus.publish("", jsonOutput);
						} catch (IOException e) {
							ws.reject();
						}
					}
				});
		 
			}
		}).listen(8090);

	}

	@Override
	public void stop() throws Exception {

	}

}