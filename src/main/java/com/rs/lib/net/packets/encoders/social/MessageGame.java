package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Account;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class MessageGame extends PacketEncoder {
	
	private MessageType type;
	private String message;
	private Account target;

	public MessageGame(MessageType type, String message, Account target) {
		super(ServerPacket.GAME_MESSAGE);
		this.type = type;
		this.message = message;
		this.target = target;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		int maskData = 0;
		if (message.length() >= 248)
			message = message.substring(0, 248);

		if (target != null) {
			maskData |= 0x1;
			if (target.getDisplayName() != null)
				maskData |= 0x2;
		}
		stream.writeSmart(type.getValue());
		stream.writeInt(0); //junk
		stream.writeByte(maskData);
		if (target != null) {
			stream.writeString(target.getUsername());
			if (target.getDisplayName() != null)
				stream.writeString(target.getDisplayName());
		}
		stream.writeString(message);
	}

	public enum MessageType {
		UNFILTERABLE(0),
		FILTERABLE(109),
		DEV_CONSOLE_CLEAR(98),
		DEV_CONSOLE(99),
		TRADE_REQUEST(100);
		
		private int value;
		
		private MessageType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}
}
