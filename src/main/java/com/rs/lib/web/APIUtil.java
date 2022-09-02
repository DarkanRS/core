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
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.web;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.rs.lib.file.JsonFileManager;
import com.rs.lib.util.Logger;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIUtil {
	
	private static OkHttpClient client = new OkHttpClient.Builder()
			.readTimeout(10, TimeUnit.SECONDS)
			.build();
			
	public static void sendResponse(HttpServerExchange exchange, int stateCode, Object responseObject) {
		try {
			exchange.setStatusCode(stateCode);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send(JsonFileManager.toJson(responseObject));
		} catch(Throwable e) {
			Logger.handle(new Object() {}, e);
		}
	}
	
	public static <T> void readJSON(HttpServerExchange ex, Class<T> clazz, Consumer<T> cb) {
		ex.getRequestReceiver().receiveFullBytes((e, m) -> {
			try {
				T obj = JsonFileManager.fromJSONString(new String(m), clazz);
				cb.accept(obj);
			} catch (Throwable t) {
				Logger.handle(new Object() {}, t);
				sendResponse(ex, StatusCodes.BAD_REQUEST, new ErrorResponse("Error parsing body."));
			}
	    });
	}

	public static boolean authenticate(HttpServerExchange ex, String key) {
		HeaderValues header = ex.getRequestHeaders().get("key");
		if (header != null && header.getFirst() != null && header.getFirst().equals(key))
			return true;
		return false;
	}
	
	public static <T> void post(Class<T> returnType, Object body, String url, String apiKey, Consumer<T> cb) {
		Object ctx = new Object() {};
		if (body == null) {
			Logger.error(ctx, "Async POST connection attempted with null body " + url);
			cb.accept(null);
		}
		Logger.trace(ctx, "Sending request: " + url);
		try {
			Builder builder = new Request.Builder()
					.url(url)
					.post(RequestBody.create(JsonFileManager.toJson(body), MediaType.parse("application/json")))
					.header("accept", "application/json");
			
			if (apiKey != null)
				builder.header("key", apiKey);
			
			Request request = builder.build();
	
			Call call = client.newCall(request);
			call.enqueue(new Callback() {
				public void onResponse(Call call, Response response) {
					try {
						String json = response.body().string();
						Logger.trace(ctx, "Request finished: " + json);
						if (returnType != null) {
							if (cb != null)
								cb.accept(JsonFileManager.fromJSONString(json, returnType));
						} else {
							if (cb != null)
								cb.accept(null);
						}
					} catch (Throwable e) {
						if (e instanceof ConnectException) {
							Logger.error(ctx, "Async POST Connection timed out to " + url);
							cb.accept(null);
							return;
						}
						Logger.handle(ctx, "Error parsing body...", e);
						if (cb != null)
							cb.accept(null);
					}
				}
	
				public void onFailure(Call call, IOException e) {
					Logger.trace(ctx, "Request failed...");
					if (cb != null)
						cb.accept(null);
				}
			});
		} catch(Throwable e) {
			Logger.handle(ctx, "Error sending async post request.", e);
		}
	}
	
	public static <T> T postSync(Class<T> returnType, Object body, String url, String apiKey) {
		Object ctx = new Object() {};
		Logger.trace(ctx, "Sending request: " + url);
		if (body == null) {
			Logger.error(ctx, "POST connection attempted with null body " + url);
			return null;
		}
		try {
			Builder builder = new Request.Builder()
					.url(url)
					.post(RequestBody.create(JsonFileManager.toJson(body), MediaType.parse("application/json")))
					.header("accept", "application/json");
			
			if (apiKey != null)
				builder.header("key", apiKey);
			
			Request request = builder.build();
	
			Call call = client.newCall(request);
			try {
				Response response = call.execute();
				String json = response.body().string();
				Logger.trace(ctx, "Request finished: " + json);
				try {
					if (returnType != null)
						return JsonFileManager.fromJSONString(json, returnType);
					return null;
				} catch(Throwable e) {
					Logger.handle(ctx, "Error parsing body...", e);
					return null;
				}
			} catch(Throwable e) {
				if (e instanceof ConnectException) {
					Logger.error(ctx, "POST Connection timed out to " + url);
					return null;
				}
				Logger.handle(ctx, "Request failed...", e);
				return null;
			}
		} catch(Throwable e) {
			Logger.handle(ctx, "Error sending post request.", e);
			return null;
		}
	}
	
	public static <T> void get(Class<T> returnType, String url, String apiKey, Consumer<T> cb) {
		Object ctx = new Object() {};
		Logger.trace(ctx, "Sending request: " + url);
		try {
			Builder builder = new Request.Builder()
					.url(url)
					.get()
					.header("accept", "application/json");
			
			if (apiKey != null)
				builder.header("key", apiKey);
			
			Request request = builder.build();
	
			Call call = client.newCall(request);
			call.enqueue(new Callback() {
				public void onResponse(Call call, Response response) {
					try {
						String json = response.body().string();
						Logger.trace(ctx, "Request finished: " + json);
						if (returnType != null) {
							if (cb != null)
								cb.accept(JsonFileManager.fromJSONString(json, returnType));
						} else {
							if (cb != null)
								cb.accept(null);
						}
					} catch (Throwable e) {
						if (e instanceof ConnectException) {
							Logger.error(ctx, "Async GET Connection timed out to " + url);
							cb.accept(null);
							return;
						}
						Logger.handle(ctx, "Error parsing body...", e);
						if (cb != null)
							cb.accept(null);
					}
				}
	
				public void onFailure(Call call, IOException e) {
					Logger.handle(ctx, "Get request failed... " + url, e);
					if (cb != null)
						cb.accept(null);
				}
			});
		} catch(Throwable e) {
			Logger.handle(ctx, "Error sending async get request.", e);
		}
	}
	
	public static <T> T getSync(Class<T> returnType, String url, String apiKey) {
		Object ctx = new Object() {};
		Logger.trace(ctx, "Sending request: " + url);
		try {
			Builder builder = new Request.Builder()
					.url(url)
					.get()
					.header("accept", "application/json");
			
			if (apiKey != null)
				builder.header("key", apiKey);
			
			Request request = builder.build();
	
			Call call = client.newCall(request);
			try {
				Response response = call.execute();
				String json = response.body().string();
				Logger.trace(ctx, "Request finished: " + json);
				try {
					if (returnType != null)
						return JsonFileManager.fromJSONString(json, returnType);
					return null;
				} catch(Throwable e) {
					Logger.handle(ctx, "Error parsing body into " + returnType, e);
					return null;
				}
			} catch(Throwable e) {
				if (e instanceof ConnectException) {
					Logger.error(ctx, "GET Connection timed out to " + url);
					return null;
				}
				Logger.handle(ctx, "Request failed...", e);
				return null;
			}
		} catch(Throwable e) {
			Logger.handle(ctx, "Error sending get request.", e);
			return null;
		}
	}

	public static Optional<String> pathParam(HttpServerExchange exchange, String name) {
		return Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst);
	}

	public static Optional<Long> pathParamAsLong(HttpServerExchange exchange, String name) {
		return pathParam(exchange, name).map(Long::parseLong);
	}

	public static Optional<Integer> pathParamAsInteger(HttpServerExchange exchange, String name) {
		return pathParam(exchange, name).map(Integer::parseInt);
	}

}
