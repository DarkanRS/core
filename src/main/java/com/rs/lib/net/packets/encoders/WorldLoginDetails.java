package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class WorldLoginDetails extends PacketEncoder {

	private int rights;
	private int pid;
	private String displayName;
	
	public WorldLoginDetails(int rights, int pid, String displayName) {
		super(ServerPacket.WORLD_LOGIN_DETAILS);
		this.rights = rights;
		this.pid = pid;
		this.displayName = displayName;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(rights);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(0);
		stream.writeByte(1);
		stream.writeByte(0);
		stream.writeShort(pid);
		stream.writeByte(1);
		stream.write24BitInteger(0);
		stream.writeByte(1); // is member world
		stream.writeString(displayName);
	}

}
