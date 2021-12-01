// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
	private int port;
	protected RoutingHandler routes = Handlers.routing();
	private APIServer server;
	
	public WebAPI(String prefixPath, int port) {
		this.prefixPath = prefixPath;
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
		System.out.println("Starting " + getClass().getSimpleName() + " on 0.0.0.0:" + port);
		server = new APIServer(prefixPath, port, new ExceptionHandler(routes) {
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception {
				System.out.println("Request: <" + exchange.getRequestMethod() + " " + exchange.getRequestURI() + " " + exchange.getProtocol() + "> from " + exchange.getSourceAddress());
				super.handleRequest(exchange);
			}
		});
		server.start();
		System.out.println(getClass().getSimpleName() + " listening on 0.0.0.0:" + port);
	}
	
}
