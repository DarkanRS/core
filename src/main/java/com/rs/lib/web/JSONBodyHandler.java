package com.rs.lib.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

import com.rs.lib.file.JsonFileManager;

public class JSONBodyHandler<T> implements HttpResponse.BodyHandler<Supplier<T>> {

	private final Class<T> targetClass;

	public JSONBodyHandler(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	@Override
	public HttpResponse.BodySubscriber<Supplier<T>> apply(HttpResponse.ResponseInfo responseInfo) {
		return asJSON(this.targetClass);
	}

	public static <W> HttpResponse.BodySubscriber<Supplier<W>> asJSON(Class<W> targetType) {
		HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();
		return HttpResponse.BodySubscribers.mapping(upstream, inputStream -> toSupplierOfType(inputStream, targetType));
	}

	public static <W> Supplier<W> toSupplierOfType(InputStream inputStream, Class<W> targetType) {
		return () -> {
			try (InputStream stream = inputStream) {
				return JsonFileManager.getGson().fromJson(new InputStreamReader(stream, "UTF-8"), targetType);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}
}
