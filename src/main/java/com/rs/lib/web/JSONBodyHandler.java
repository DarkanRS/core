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
