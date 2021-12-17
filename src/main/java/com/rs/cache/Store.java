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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Arrays;

import com.rs.cache.utils.CacheUtil;
import com.rs.cache.utils.Whirlpool;
import com.rs.lib.io.OutputStream;

public final class Store {

	private Index[] indexes;
	private MainFile index255;
	private String path;
	private RandomAccessFile data;
	private Huffman huffman;
	
	public Store(String path, boolean newProtocol) throws IOException {
		this.path = path;
		data = new RandomAccessFile(path + "main_file_cache.dat2", "rw");
		index255 = new MainFile(255, data, new RandomAccessFile(path + "main_file_cache.idx255", "rw"));
		int idxsCount = index255.getArchivesCount();
		indexes = new Index[idxsCount];
		for (int id = 0; id < idxsCount; id++) {
			if (id == 47)
				continue;
			Index index = new Index(index255, new MainFile(id, data, new RandomAccessFile(path + "main_file_cache.idx" + id, "rw")));
			if (index.getTable() == null)
				continue;
			indexes[id] = index;
		}
		huffman = new Huffman(this);
	}
	
	public Store(String path) throws IOException {
		this(path, false);
	}
	
	public final byte[] getChecksumContainer(BigInteger rsaExp, BigInteger rsaMod) {
		byte[] checksumTable = getChecksumTable(rsaExp, rsaMod);
		OutputStream out = new OutputStream();
		out.writeByte(255);
		out.writeInt(255);
		out.writeByte(0);
		out.writeInt(checksumTable.length);
		int offset = 10;
		for (int index = 0; index < checksumTable.length; index++) {
			if (offset == 512) {
				out.writeByte(255);
				offset = 1;
			}
			out.writeByte(checksumTable[index]);
			offset++;
		}
		return out.toByteArray();
	}

	public final byte[] getChecksumTable(BigInteger rsaExp, BigInteger rsaMod) {
		OutputStream os = new OutputStream();
		os.writeByte(indexes.length);
		for (int i = 0;i < indexes.length;i++) {
			os.writeInt(indexes[i].getCRC());
			os.writeInt(indexes[i].getTable().getRevision());
			os.writeBytes(indexes[i].getWhirlpool());
		}
		
		byte[] archive = os.toByteArray();

		OutputStream temp = new OutputStream(65);
		temp.writeByte(0);
		temp.writeBytes(Whirlpool.getWhirlpool(archive, 0, archive.length));
		os.writeBytes(CacheUtil.cryptRSA(temp.toByteArray(), rsaExp, rsaMod));
		return os.toByteArray();
	}
	
	public Index getIndex(IndexType index) {
		return indexes[index.ordinal()];
	}
	
	public Index[] getIndices() {
		return indexes;
	}

	public MainFile getIndex255() {
		return index255;
	}

	/*
	 * returns index
	 */
	public int addIndex(boolean named, boolean usesWhirpool, int tableCompression) throws IOException {
		int id = indexes.length;
		Index[] newIndexes = Arrays.copyOf(indexes, indexes.length + 1);
		resetIndex(id, newIndexes, named, usesWhirpool, tableCompression);
		indexes = newIndexes;
		return id;
	}

	public void resetIndex(int id, boolean named, boolean usesWhirpool, int tableCompression) throws FileNotFoundException, IOException {
		resetIndex(id, indexes, named, usesWhirpool, tableCompression);
	}

	public void resetIndex(int id, Index[] indexes, boolean named, boolean usesWhirpool, int tableCompression) throws FileNotFoundException, IOException {
		OutputStream stream = new OutputStream(4);
		stream.writeByte(5);
		stream.writeByte((named ? 0x1 : 0) | (usesWhirpool ? 0x2 : 0));
		stream.writeShort(0);
		byte[] archiveData = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(archiveData, 0, archiveData.length);
		Archive archive = new Archive(id, tableCompression, -1, archiveData);
		index255.putArchiveData(id, archive.compress());
		indexes[id] = new Index(index255, new MainFile(id, data, new RandomAccessFile(path + "main_file_cache.idx" + id, "rw")));
	}

	public Huffman getHuffman() {
		return huffman;
	}
}
