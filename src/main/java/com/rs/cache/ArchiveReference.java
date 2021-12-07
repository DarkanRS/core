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

public class ArchiveReference {

	private int nameHash;
	private byte[] whirpool;
	private int crc;
	private int revision;
	private FileReference[] files;
	private int[] validFileIds;
	private boolean needsFilesSort;
	private boolean updatedRevision;

	public void updateRevision() {
		if (updatedRevision)
			return;
		revision++;
		updatedRevision = true;
	}

	public int getNameHash() {
		return nameHash;
	}

	public void setNameHash(int nameHash) {
		this.nameHash = nameHash;
	}

	public byte[] getWhirpool() {
		return whirpool;
	}

	public void setWhirpool(byte[] whirpool) {
		this.whirpool = whirpool;
	}

	public int getCRC() {
		return crc;
	}

	public void setCrc(int crc) {
		this.crc = crc;
	}

	public int getRevision() {
		return revision;
	}

	public FileReference[] getFiles() {
		return files;
	}

	public void setFiles(FileReference[] files) {
		this.files = files;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public int[] getValidFileIds() {
		return validFileIds;
	}

	public void setValidFileIds(int[] validFileIds) {
		this.validFileIds = validFileIds;
	}

	public boolean isNeedsFilesSort() {
		return needsFilesSort;
	}

	public void setNeedsFilesSort(boolean needsFilesSort) {
		this.needsFilesSort = needsFilesSort;
	}

	public void removeFileReference(int fileId) {
		int[] newValidFileIds = new int[validFileIds.length - 1];
		int count = 0;
		for (int id : validFileIds) {
			if (id == fileId)
				continue;
			newValidFileIds[count++] = id;
		}
		validFileIds = newValidFileIds;
		files[fileId] = null;
	}

	public void addEmptyFileReference(int fileId) {
		needsFilesSort = true;
		int[] newValidFileIds = Arrays.copyOf(validFileIds, validFileIds.length + 1);
		newValidFileIds[newValidFileIds.length - 1] = fileId;
		validFileIds = newValidFileIds;
		if (files.length <= fileId) {
			FileReference[] newFiles = Arrays.copyOf(files, fileId + 1);
			newFiles[fileId] = new FileReference();
			files = newFiles;
		} else
			files[fileId] = new FileReference();
	}

	public void sortFiles() {
		Arrays.sort(validFileIds);
		needsFilesSort = false;
	}

	public void reset() {
		whirpool = null;
		updatedRevision = true;
		revision = 0;
		nameHash = 0;
		crc = 0;
		files = new FileReference[0];
		validFileIds = new int[0];
		needsFilesSort = false;
	}

	public void copyHeader(ArchiveReference fromReference) {
		setCrc(fromReference.getCRC());
		setNameHash(fromReference.getNameHash());
		setWhirpool(fromReference.getWhirpool());
		int[] validFiles = fromReference.getValidFileIds();
		setValidFileIds(Arrays.copyOf(validFiles, validFiles.length));
		FileReference[] files = fromReference.getFiles();
		setFiles(Arrays.copyOf(files, files.length));
	}

}
