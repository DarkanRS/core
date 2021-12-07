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

import java.util.Arrays;

import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;

public final class ReferenceTable {

	private Archive archive;
	private int revision;
	private boolean named;
	private boolean usesWhirpool;
	private ArchiveReference[] archives;
	private int[] validArchiveIds;

	// editing
	private boolean updatedRevision;
	private boolean needsArchivesSort;

	protected ReferenceTable(Archive archive) {
		this.archive = archive;
		decodeHeader();
	}

	public void setKeys(int[] keys) {
		archive.setKeys(keys);
	}

	public int[] getKeys() {
		return archive.getKeys();
	}

	public void sortArchives() {
		Arrays.sort(validArchiveIds);
		needsArchivesSort = false;
	}

	public void addEmptyArchiveReference(int archiveId) {
		needsArchivesSort = true;
		int[] newValidArchiveIds = Arrays.copyOf(validArchiveIds, validArchiveIds.length + 1);
		newValidArchiveIds[newValidArchiveIds.length - 1] = archiveId;
		validArchiveIds = newValidArchiveIds;
		ArchiveReference reference;
		if (archives.length <= archiveId) {
			ArchiveReference[] newArchives = Arrays.copyOf(archives, archiveId + 1);
			reference = newArchives[archiveId] = new ArchiveReference();
			archives = newArchives;
		} else
			reference = archives[archiveId] = new ArchiveReference();
		reference.reset();
	}

	public void sortTable() {
		if (needsArchivesSort)
			sortArchives();
		for (int index = 0; index < validArchiveIds.length; index++) {
			ArchiveReference archive = archives[validArchiveIds[index]];
			if (archive.isNeedsFilesSort())
				archive.sortFiles();
		}
	}

	public Object[] encodeHeader(MainFile mainFile) {
		OutputStream stream = new OutputStream();
		int protocol = getProtocol();
		stream.writeByte(protocol);
		if (protocol >= 6)
			stream.writeInt(revision);
		stream.writeByte((named ? 0x1 : 0) | (usesWhirpool ? 0x2 : 0));
		if (protocol >= 7)
			stream.writeBigSmart(validArchiveIds.length);
		else
			stream.writeShort(validArchiveIds.length);
		for (int index = 0; index < validArchiveIds.length; index++) {
			int offset = validArchiveIds[index];
			if (index != 0)
				offset -= validArchiveIds[index - 1];
			if (protocol >= 7)
				stream.writeBigSmart(offset);
			else
				stream.writeShort(offset);
		}
		if (named)
			for (int index = 0; index < validArchiveIds.length; index++)
				stream.writeInt(archives[validArchiveIds[index]].getNameHash());
		for (int index = 0; index < validArchiveIds.length; index++)
			stream.writeInt(archives[validArchiveIds[index]].getCRC());
		if (usesWhirpool)
			for (int index = 0; index < validArchiveIds.length; index++)
				stream.writeBytes(archives[validArchiveIds[index]].getWhirpool());
		for (int index = 0; index < validArchiveIds.length; index++)
			stream.writeInt(archives[validArchiveIds[index]].getRevision());
		for (int index = 0; index < validArchiveIds.length; index++) {
			int value = archives[validArchiveIds[index]].getValidFileIds().length;
			if (protocol >= 7)
				stream.writeBigSmart(value);
			else
				stream.writeShort(value);
		}
		for (int index = 0; index < validArchiveIds.length; index++) {
			ArchiveReference archive = archives[validArchiveIds[index]];
			for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++) {
				int offset = archive.getValidFileIds()[index2];
				if (index2 != 0)
					offset -= archive.getValidFileIds()[index2 - 1];
				if (protocol >= 7)
					stream.writeBigSmart(offset);
				else
					stream.writeShort(offset);
			}
		}
		if (named) {
			for (int index = 0; index < validArchiveIds.length; index++) {
				ArchiveReference archive = archives[validArchiveIds[index]];
				for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++)
					stream.writeInt(archive.getFiles()[archive.getValidFileIds()[index2]].getNameHash());
			}
		}
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return archive.editNoRevision(data, mainFile);
	}

	public int getProtocol() {
		if (archives.length > 65535)
			return 7;
		for (int index = 0; index < validArchiveIds.length; index++) {
			if (index > 0)
				if (validArchiveIds[index] - validArchiveIds[index - 1] > 65535)
					return 7;
			if (archives[validArchiveIds[index]].getValidFileIds().length > 65535)
				return 7;
		}
		return revision == 0 ? 5 : 6;
	}

	public void setRevision(int revision) {
		updatedRevision = true;
		this.revision = revision;
	}

	public void updateRevision() {
		if (updatedRevision)
			return;
		revision++;
		updatedRevision = true;
	}

	private void decodeHeader() {
		InputStream stream = new InputStream(archive.getData());
		int protocol = stream.readUnsignedByte();
		if (protocol < 5 || protocol > 7)
			throw new RuntimeException("INVALID PROTOCOL");
		if (protocol >= 6)
			revision = stream.readInt();
		int hash = stream.readUnsignedByte();
		named = (0x1 & hash) != 0;
		usesWhirpool = (0x2 & hash) != 0;
		int validArchivesCount = protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort();
		validArchiveIds = new int[validArchivesCount];
		int lastArchiveId = 0;
		int biggestArchiveId = 0;
		for (int index = 0; index < validArchivesCount; index++) {
			int archiveId = lastArchiveId += protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort();
			if (archiveId > biggestArchiveId)
				biggestArchiveId = archiveId;
			validArchiveIds[index] = archiveId;
		}
		archives = new ArchiveReference[biggestArchiveId + 1];
		for (int index = 0; index < validArchivesCount; index++)
			archives[validArchiveIds[index]] = new ArchiveReference();
		if (named)
			for (int index = 0; index < validArchivesCount; index++)
				archives[validArchiveIds[index]].setNameHash(stream.readInt());
		for (int index = 0; index < validArchivesCount; index++)
			archives[validArchiveIds[index]].setCrc(stream.readInt());
		if (usesWhirpool) {
			for (int index = 0; index < validArchivesCount; index++) {
				byte[] whirpool = new byte[64];
				stream.getBytes(whirpool, 0, 64);
				archives[validArchiveIds[index]].setWhirpool(whirpool);
			}
		}
		for (int index = 0; index < validArchivesCount; index++)
			archives[validArchiveIds[index]].setRevision(stream.readInt());
		for (int index = 0; index < validArchivesCount; index++)
			archives[validArchiveIds[index]].setValidFileIds(new int[protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort()]);
		for (int index = 0; index < validArchivesCount; index++) {
			int lastFileId = 0;
			int biggestFileId = 0;
			ArchiveReference archive = archives[validArchiveIds[index]];
			for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++) {
				int fileId = lastFileId += protocol >= 7 ? stream.readBigSmart() : stream.readUnsignedShort();
				if (fileId > biggestFileId)
					biggestFileId = fileId;
				archive.getValidFileIds()[index2] = fileId;
			}
			archive.setFiles(new FileReference[biggestFileId + 1]);
			for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++)
				archive.getFiles()[archive.getValidFileIds()[index2]] = new FileReference();
		}
		if (named) {
			for (int index = 0; index < validArchivesCount; index++) {
				ArchiveReference archive = archives[validArchiveIds[index]];
				for (int index2 = 0; index2 < archive.getValidFileIds().length; index2++)
					archive.getFiles()[archive.getValidFileIds()[index2]].setNameHash(stream.readInt());
			}
		}
	}

	public int getRevision() {
		return revision;
	}

	public ArchiveReference[] getArchives() {
		return archives;
	}

	public int[] getValidArchiveIds() {
		return validArchiveIds;
	}

	public boolean isNamed() {
		return named;
	}

	public boolean usesWhirpool() {
		return usesWhirpool;
	}

	public int getCompression() {
		return archive.getCompression();
	}

}