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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.JsonIOException;
import com.rs.lib.file.JsonFileManager;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIUtil {
	
	private static OkHttpClient client = new OkHttpClient.Builder()
			.readTimeout(10, TimeUnit.SECONDS)
			.build();
			
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
		Request request = new Request.Builder()
				.url(url)
				.post(RequestBody.create(JsonFileManager.toJson(body), MediaType.parse("application/json")))
				.header("accept", "application/json")
				.header("key", apiKey)
				.build();

		Call call = client.newCall(request);
		call.enqueue(new Callback() {
			public void onResponse(Call call, Response response) throws IOException {
				String json = response.body().string();
				java.util.logging.Logger.getLogger("Web").finest("Request finished: " + json);
				try {
					if (returnType != null) {
						if (cb != null)
							cb.accept(JsonFileManager.fromJSONString(json, returnType));
					} else {
						if (cb != null)
							cb.accept(null);
					}
				} catch (Exception e) {
					System.err.println("Error parsing body into " + returnType + ": " + json);
					e.printStackTrace();
					if (cb != null)
						cb.accept(null);
				}
			}

			public void onFailure(Call call, IOException e) {
				java.util.logging.Logger.getLogger("Web").finest("Request failed...");
				if (cb != null)
					cb.accept(null);
			}
		});
	}
	
	public static <T> T postSync(Class<T> returnType, Object body, String url, String apiKey) throws InterruptedException, ExecutionException, IOException {
		java.util.logging.Logger.getLogger("Web").finest("Sending request: " + url);
		Request request = new Request.Builder()
				.url(url)
				.post(RequestBody.create(JsonFileManager.toJson(body), MediaType.parse("application/json")))
				.header("accept", "application/json")
				.header("key", apiKey)
				.build();

		Call call = client.newCall(request);
		try {
			Response response = call.execute();
			String json = response.body().string();
			java.util.logging.Logger.getLogger("Web").finest("Request finished: " + json);
			try {
				if (returnType != null)
					return JsonFileManager.fromJSONString(json, returnType);
				return null;
			} catch(Exception e) {
				System.err.println("Error parsing body into " + returnType + ": " + json);
				e.printStackTrace();
				return null;
			}
		} catch(Exception e) {
			java.util.logging.Logger.getLogger("Web").finest("Request failed...");
			return null;
		}
	}
	
}
