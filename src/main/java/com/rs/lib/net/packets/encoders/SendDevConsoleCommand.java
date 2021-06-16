package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class SendDevConsoleCommand extends PacketEncoder {

	private String command;
	
	public SendDevConsoleCommand(String command) {
		super(ServerPacket.PROCESS_DEV_CONSOLE_COMMAND);
		this.command = command;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeString(command);
	}

}
