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
package com.rs.cache.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.rs.cache.utils.bzip2.CBZip2InputStream;
import com.rs.cache.utils.bzip2.CBZip2OutputStream;

public class CompressionUtils {
	
	public static byte[] bunzip2(byte[] bytes) {
		try {
			byte[] bzip2 = new byte[bytes.length + 2];
			bzip2[0] = 'h';
			bzip2[1] = '1';
			System.arraycopy(bytes, 0, bzip2, 2, bytes.length);
	
			InputStream is = new CBZip2InputStream(new ByteArrayInputStream(bzip2));
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					byte[] buf = new byte[4096];
					int len = 0;
					while ((len = is.read(buf, 0, buf.length)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					os.close();
				}
	
				return os.toByteArray();
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] bzip2(byte[] bytes) {
		try {
			InputStream is = new ByteArrayInputStream(bytes);
			try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				OutputStream os = new CBZip2OutputStream(bout, 1);
				try {
					byte[] buf = new byte[4096];
					int len = 0;
					while ((len = is.read(buf, 0, buf.length)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					os.close();
				}
	
				bytes = bout.toByteArray();
				byte[] bzip2 = new byte[bytes.length - 2];
				System.arraycopy(bytes, 2, bzip2, 0, bzip2.length);
				return bzip2;
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] gunzip(byte[] bytes) {
		try {
			InputStream is = new GZIPInputStream(new ByteArrayInputStream(bytes));
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					byte[] buf = new byte[4096];
					int len = 0;
					while ((len = is.read(buf, 0, buf.length)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					os.close();
				}
	
				return os.toByteArray();
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] gzip(byte[] bytes) {
		try {
			InputStream is = new ByteArrayInputStream(bytes);
			try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				OutputStream os = new GZIPOutputStream(bout);
				try {
					byte[] buf = new byte[4096];
					int len = 0;
					while ((len = is.read(buf, 0, buf.length)) != -1) {
						os.write(buf, 0, len);
					}
				} finally {
					os.close();
				}
	
				return bout.toByteArray();
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
