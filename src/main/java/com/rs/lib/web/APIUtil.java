package com.rs.lib.web;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.gson.JsonIOException;
import com.rs.lib.file.JsonFileManager;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class APIUtil {
	
	private static ExecutorService API_REQUEST_POOL = Executors.newFixedThreadPool(10);
	
	public static void sendResponse(HttpServerExchange exchange, int stateCode, Object responseObject) {
		exchange.setStatusCode(stateCode);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(JsonFileManager.toJson(responseObject));
	}
	
	public static <T> void readJSON(HttpServerExchange ex, Class<T> clazz, Consumer<T> cb) {
		ex.getRequestReceiver().receiveFullBytes((e, m) -> {
			try {
				T obj = JsonFileManager.fromJSONString(new String(m), clazz);
				cb.accept(obj);
			} catch (JsonIOException | IOException e1) {
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
		API_REQUEST_POOL.execute(() -> {
			cb.accept(postSync(returnType, body, url, apiKey));
		});
	}
	
	public static <T> T postSync(Class<T> returnType, Object body, String url, String apiKey) {
			try {
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder(URI.create(url)).POST(HttpRequest.BodyPublishers.ofString(JsonFileManager.toJson(body))).header("accept", "application/json").header("key", apiKey).build();
				CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
				HttpResponse<String> response = future.get();
				return JsonFileManager.fromJSONString(response.body(), returnType);
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
	}
	
}
