package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.REPORT_ABUSE)
public class ReportAbuse extends Packet {
	
	private String username;
	private int type;
	private boolean mute;

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		ReportAbuse p = new ReportAbuse();
		this.username = stream.readString();
		this.type = stream.readUnsignedByte();
		this.mute = stream.readUnsignedByte() == 1;
		stream.readString();
		return p;
	}

	public String getUsername() {
		return username;
	}

	public int getType() {
		return type;
	}

	public boolean isMute() {
		return mute;
	}

}
