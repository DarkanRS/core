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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.google.gson.JsonIOException;
import com.rs.lib.file.JsonFileManager;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class APIUtil {
	
	private static HttpClient CLIENT = HttpClient.newHttpClient();
		
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
		java.util.logging.Logger.getLogger("Web").finest("Sending request: " + url);
		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(2)).POST(HttpRequest.BodyPublishers.ofString(JsonFileManager.toJson(body))).header("accept", "application/json").header("key", apiKey).build();
		CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
		.thenApply(HttpResponse::body)
		.thenAccept(res -> {
			try {
				cb.accept(JsonFileManager.fromJSONString(res, returnType));
			} catch (JsonIOException | IOException e) {
				System.err.println("Exception handling POST " + url + " - " + e.getMessage());
			}
		});
	}
	
	public static <T> T postSync(Class<T> returnType, Object body, String url, String apiKey) throws InterruptedException, ExecutionException, IOException {
		java.util.logging.Logger.getLogger("Web").finest("Sending request: " + url);
		HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(Duration.ofSeconds(2)).POST(HttpRequest.BodyPublishers.ofString(JsonFileManager.toJson(body))).header("accept", "application/json").header("key", apiKey).build();
		HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		java.util.logging.Logger.getLogger("Web").finest("Request finished: " + response.body());
		if (returnType == null)
			return null;
		try {
			return JsonFileManager.fromJSONString(response.body(), returnType);
		} catch (Exception e) {
			System.err.println("Error parsing body into " + returnType + ": " + response.body());
			return null;
		}
	}
	
}
