package com.soma2.chatserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RealServer extends AbstractVerticle {

	HttpServer httpServer;
	
	@Override
	public void start() throws Exception{
		super.start();

		httpServer = vertx.createHttpServer();
    
		Router router = Router.router(vertx);
		Route route = router.route("/:METHOD");

		
		httpServer.requestHandler(new Handler<HttpServerRequest>() {
			
			@Override
			public void handle(HttpServerRequest request) {
				MultiMap params = request.params();

				String method = params.get("METHOD");
				System.out.println(request.path());
				System.out.println(request.params().toString());

				request.endHandler(new Handler<Void>() {
					
					@Override
					public void handle(Void args) {
						evenBus
//						JsonObject param = new JsonObject();
//						params.forEach(entry -> param.put(entry.getKey(), entry.getValue()));
////						request.response().setStatusCode(200);
////				        request.response().putHeader("content-type", "Access-Control-Allow-Methods: POST, GET, OPTIONS, DELETE" );
////				        request.response().putHeader("content-type", "Access-Control-Max-Age: 1000" );
////				        request.response().putHeader("content-type", "Access-Control-Allow-Headers: Content-Typeh" );
//						request.response().putHeader("content-type", "Access-Control-Allow-Origin: *" );
////						request.response().putHeader("content-type", "text/x-cross-domain-policy" );
//
////						request.response().putHeader("Access-Control-Allow-Origin", "*" );
//						request.response().putHeader("content-type", "application/json");
//						request.response().end("{\"METHOD\": \"" + method + "\"}");	
					}
				});
				
			}
		}).listen(8888);
		
		
	}
	
	
}
