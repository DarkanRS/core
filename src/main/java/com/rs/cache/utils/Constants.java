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
package com.rs.cache.utils;

public final class Constants {

	public static final int NO_COMPRESSION = 0;
	public static final int BZIP2_COMPRESSION = 1;
	public static final int GZIP_COMPRESSION = 2;

	public static final int MAX_VALID_ARCHIVE_LENGTH = 1000000000;

	public static final int CLIENT_BUILD = 727;
	public static final boolean ENCRYPTED_CACHE = false;

	private Constants() {

	}
}
