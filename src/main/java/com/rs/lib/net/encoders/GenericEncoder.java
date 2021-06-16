package com.rs.lib.net.encoders;

import com.rs.lib.net.Encoder;
import com.rs.lib.net.Session;
import com.rs.lib.net.packets.PacketEncoder;

public class GenericEncoder extends Encoder {

	public GenericEncoder(Session session) {
		super(session);
	}

	public void write(PacketEncoder packet) {
		session.writeToQueue(packet);
	}
}
