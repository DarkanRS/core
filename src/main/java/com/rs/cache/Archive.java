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
package com.rs.cache;

import com.rs.cache.utils.CacheUtil;
import com.rs.cache.utils.CompressionUtils;
import com.rs.cache.utils.Constants;
import com.rs.cache.utils.Whirlpool;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;

public class Archive {

	private int id;
	private int revision;
	private int compression;
	private byte[] data;
	private int[] keys;

	protected Archive(int id, byte[] archive, int[] keys) {
		this.id = id;
		this.keys = keys;
		decompress(archive);
		
	}

	public Archive(int id, int compression, int revision, byte[] data) {
		this.id = id;
		this.compression = compression;
		this.revision = revision;
		this.data = data;
	}

	public byte[] compress() {
		OutputStream stream = new OutputStream();
		stream.writeByte(compression);
		byte[] compressedData;
		switch (compression) {
		case Constants.GZIP_COMPRESSION:
			compressedData = CompressionUtils.gzip(data);
			stream.writeInt(compressedData.length);
			stream.writeInt(data.length);
			break;
		case Constants.BZIP2_COMPRESSION:
			compressedData = CompressionUtils.bzip2(data);
			stream.writeInt(compressedData.length);
			stream.writeInt(data.length);
			break;
		default: 
			compressedData = data;
			stream.writeInt(data.length);
			break;
		}
		stream.writeBytes(compressedData);
		if (keys != null && keys.length == 4)
			stream.encodeXTEA(keys, 5, stream.getOffset());
		if (revision != -1)
			stream.writeShort(revision);
		return stream.toByteArray();
	}

	private void decompress(byte[] archive) {
		try {
			InputStream stream = new InputStream(archive);
			compression = stream.readUnsignedByte();
			int length = stream.readInt();
			
			if (keys != null && keys.length == 4)
				stream.decodeXTEA(keys, 5, length + (compression == Constants.NO_COMPRESSION ? 5 : 9));
			
			if (length < 0 || length > Constants.MAX_VALID_ARCHIVE_LENGTH)
				throw new RuntimeException("INVALID ARCHIVE HEADER");
			switch (compression) {
			case Constants.NO_COMPRESSION:
				data = new byte[length];
				stream.readBytes(data, 0, length);
				
				revision = -1;
				if (stream.getRemaining() >= 2) {
					revision = stream.readShort();
				}
				break;
			case Constants.BZIP2_COMPRESSION:
			case Constants.GZIP_COMPRESSION:
				int uncompressedLength = stream.readInt();
				if (uncompressedLength <= 0 || uncompressedLength > 1000000000) {
					data = null;
					break;
				}
				byte[] compressed = new byte[length];
				stream.readBytes(compressed, 0, length);
				data = (compression == Constants.BZIP2_COMPRESSION ? CompressionUtils.bunzip2(compressed) : CompressionUtils.gunzip(compressed));
				
				if (data.length != uncompressedLength) {
					throw new Exception("Length mismatch. [ " + data.length + ", " + uncompressedLength + " ]");
				}
				
				revision = -1;
				if (stream.getRemaining() >= 2) {
					revision = stream.readShort();
				}
				break;
			}
		} catch (Exception e) {
			System.out.println("Exception loading archive: " + id);
		}
	}

	public Object[] editNoRevision(byte[] data, MainFile mainFile) {
		this.data = data;
		byte[] compressed = compress();
		if (!mainFile.putArchiveData(id, compressed))
			return null;
		return new Object[] { CacheUtil.getCrcChecksum(compressed, compressed.length), Whirlpool.getWhirlpool(compressed, 0, compressed.length) };
	}

	public int getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	public int getDecompressedLength() {
		return data.length;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public int getCompression() {
		return compression;
	}

	public int[] getKeys() {
		return keys;
	}

	public void setKeys(int[] keys) {
		this.keys = keys;
	}

}