package com.rs.lib.game;

public class QuickChatMessage extends PublicChatMessage {

	private int fileId;
	private byte[] data;

	public QuickChatMessage(int fileId, byte[] data) {
		super(data == null ? null : new String(data), 0x8000);
		this.fileId = fileId;
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

}
