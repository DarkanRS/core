package com.rs.lib.net.packets.encoders.vars;

import com.rs.cache.loaders.ClanVarDefinitions;
import com.rs.cache.loaders.cs2.CS2Type;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class VarClan extends PacketEncoder {
	private int id;
	private Object value;
	
	public VarClan(int id, Object value) {
		super(getPacketForValue(id, value));
		
	}

	private static ServerPacket getPacketForValue(int id, Object value) {
		ClanVarDefinitions def = ClanVarDefinitions.getDefs(id);
		if (def.type == CS2Type.STRING) {
			if (!(value instanceof String))
				throw new RuntimeException("Clan var " + id + " should be a string.");
			return ServerPacket.SET_CLAN_STRING;
		}
		if (def.type == CS2Type.LONG) {
			if (!(value instanceof Long) && !(value instanceof Integer))
				throw new RuntimeException("Clan var " + id + " should be a long or integer.");
			return ServerPacket.VARCLAN_SET_LONG;
		}
		if (def.type == CS2Type.INT) {
			if (!(value instanceof Long) && !(value instanceof Integer))
				throw new RuntimeException("Clan var " + id + " should be a long or integer.");
			return ((long) value < Byte.MIN_VALUE || (long) value > Byte.MAX_VALUE) ? ServerPacket.VARCLAN_SET_INT : ServerPacket.VARCLAN_SET_BYTE;
		}
		throw new RuntimeException("Error getting packet for clan var " + id);
	}

	@Override
	public void encodeBody(OutputStream stream) {
		switch(getPacket()) {
		case SET_CLAN_STRING -> {
			stream.writeShort(id);
			stream.writeString((String) value);
		}
		case VARCLAN_SET_LONG -> {
			stream.writeShort(id);
			stream.writeLong((long) value);
		}
		case VARCLAN_SET_BYTE -> {
			stream.writeShort(id);
			stream.writeByte((int) value);
		}
		case VARCLAN_SET_INT -> {
			stream.writeShort(id);
			stream.writeInt((int) value);
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + getPacket());
		}
	}
}
