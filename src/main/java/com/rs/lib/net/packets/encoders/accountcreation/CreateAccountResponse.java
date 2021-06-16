package com.rs.lib.net.packets.encoders.accountcreation;

import com.rs.lib.io.OutputStream;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class CreateAccountResponse extends PacketEncoder {
	
	public enum CAResp {
		ERROR_CONTACTING_SERVER(1),
		LOGIN(2),
		CANNOT_CREATE_ACCOUNT_ATM(9),
		UNEXPECTED_SERVER_RESPONSE(10),
		EMAIL_ALREADY_IN_USE(20),
		INVALID_EMAIL(21),
		PLEASE_SUPPLY_VALID_PASS(30),
		PASSWORDS_MAY_ONLY_CONTAIN_LETTERS_AND_NUMBERS(31),
		PASSWORD_TOO_EASY(32);
		
		private int code;
		
		private CAResp(int code) {
			this.code = code;
		}
	}
	
	private CAResp response;
	private int code = -1;

	public CreateAccountResponse(CAResp response) {
		super(ServerPacket.CREATE_ACCOUNT_REPLY);
		this.response = response;
	}
	
	public CreateAccountResponse(int code) {
		super(ServerPacket.CREATE_ACCOUNT_REPLY);
		this.code = code;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		stream.writeByte(response != null ? response.code : code);
	}

}
