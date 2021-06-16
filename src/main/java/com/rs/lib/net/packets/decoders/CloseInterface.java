package com.rs.lib.net.packets.decoders;

import com.rs.lib.io.InputStream;
import com.rs.lib.net.ClientPacket;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketDecoder;

@PacketDecoder(ClientPacket.CLOSE_INTERFACE)
public class CloseInterface extends Packet {

	@Override
	public Packet decodeAndCreateInstance(InputStream stream) {
		return new CloseInterface();
	}

}
