package com.soma2.chatserver;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

public class ChatClient extends AbstractVerticle{
	
	
	@Override
	public void start(){
		NetClient netClient = vertx.createNetClient();
		netClient.connect(9999, "localhost"
		,new Handler<AsyncResult<NetSocket>>() {

			@Override
			public void handle(AsyncResult<NetSocket> socket) {
				socket.result().handler(new Handler<Buffer>() {

					@Override
					public void handle(Buffer buffer) {
						System.out.println(buffer);
						
					}
					
				});
				socket.result().write("Hello server");
			}

		
		
	});

	}
}
