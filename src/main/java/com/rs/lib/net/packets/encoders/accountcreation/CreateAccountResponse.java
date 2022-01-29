// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
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
