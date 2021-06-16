package com.rs.lib.web;

import com.rs.lib.util.Logger;

import io.undertow.Handlers;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.ExceptionHandler;

public class WebAPI extends Thread {
		
	public enum HTTP {
		GET,
		POST,
		PUT,
		DELETE
	}
	
	private String prefixPath;
	private String ip;
	private int port;
	protected RoutingHandler routes = Handlers.routing();
	private APIServer server;
	
	public WebAPI(String prefixPath, String ip, int port) {
		this.prefixPath = prefixPath;
		this.ip = ip;
		this.port = port;
		this.setName("WebAPI Thread");
		this.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread th, Throwable ex) {
				Logger.handle(ex);
			}
		});
	}
	
	public void start() {
		this.run();
	}
	
	public void addRoute(Route route) {
		route.build(routes);
	}
	
	@Override
	public void run() {
		System.out.println("Starting " + getClass().getSimpleName() + " on " + ip + ":" + port);
		server = new APIServer(prefixPath, port, ip, new ExceptionHandler(routes) {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				System.out.println("Request: <" + exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol() + "> from " + exchange.getSourceAddress());
				super.handleRequest(exchange);
			}
		});
		server.start();
		System.out.println(getClass().getSimpleName() + " listening on " + ip + ":" + port);
	}
	
}
