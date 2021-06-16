package com.rs.lib.web;

import io.undertow.server.RoutingHandler;

public interface Route {
	public abstract void build(RoutingHandler route);
}
