package com.rs.lib.net.packets.encoders;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;
import com.rs.lib.util.ReflectionCheck;

public class ReflectionCheckRequest extends PacketEncoder {
	
	private ReflectionCheck check;

	public ReflectionCheckRequest(ReflectionCheck check) {
		super(ServerPacket.REFLECTION_CHECK);
		this.check = check;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(1); //amount to check
		
		stream.writeInt(check.getId()); //verification

		//for size etc
		stream.writeByte(4); //type 4 = method check
		stream.writeString(check.getClassName());
		stream.writeString(check.getMethodName());

		if (check.getParams() != null && check.getParams().length > 0) {
			stream.writeByte(check.getParams().length);
			for (String param : check.getParams())
				stream.writeString(param);
		} else {
			stream.writeByte(0);
		}

		stream.writeString(check.getReturnType()); //return type
	}
}
