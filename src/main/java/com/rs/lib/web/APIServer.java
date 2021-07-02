package com.rs.lib.web;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.ExceptionHandler;

public class APIServer {

	private String prefixPath;
	private int port;
	private ExceptionHandler api;
	private Undertow server;

	public APIServer(String prefixPath, int port, ExceptionHandler api) {
		this.prefixPath = prefixPath;
		this.port = port;
		this.api = api;
	}

	public void start() {
		server = Undertow.builder()
				.addHttpListener(port, "0.0.0.0")
				.setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, true)
				.setHandler(Handlers.path().addPrefixPath(prefixPath, api))
				.build();
		server.start();
	}
	
	public Undertow getUndertow() {
		return server;
	}

}
