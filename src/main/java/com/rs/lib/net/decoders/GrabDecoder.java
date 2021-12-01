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
//  Copyright © 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.net.decoders;

import com.rs.cache.Cache;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.Decoder;
import com.rs.lib.net.Session;
import com.rs.lib.net.encoders.GrabEncoder;

public final class GrabDecoder extends Decoder {

	public GrabDecoder(Session connection) {
		super(connection);
	}

	@Override
	public final int decode(InputStream stream) {
		if (stream.getRemaining() > 750) {
			System.out.println("Incoming crash from: " + session.getIP() + " - Size: " + stream.getRemaining());
			session.getChannel().close();
		}
		while (stream.getRemaining() >= 6 && session.getChannel().isConnected()) {
			int packetId = stream.readUnsignedByte();
			if (packetId == 0 || packetId == 1)
				decodeRequestCacheContainer(stream, packetId == 1);
			else
				decodeOtherPacket(stream, packetId);
		}
		return stream.getOffset();
	}

	private final void decodeRequestCacheContainer(InputStream stream, boolean priority) {
		int indexId = stream.readUnsignedByte();
		int archiveId = stream.readInt();
		if (archiveId < 0)
			return;
		if (indexId != 255) {
			if (Cache.STORE.getIndices().length <= indexId || Cache.STORE.getIndices()[indexId] == null || !Cache.STORE.getIndices()[indexId].archiveExists(archiveId))
				return;
		} else if (archiveId != 255)
			if (Cache.STORE.getIndices().length <= archiveId || Cache.STORE.getIndices()[archiveId] == null)
				return;
		session.getEncoder(GrabEncoder.class).sendCacheArchive(indexId, archiveId, priority);
	}

	private final void decodeOtherPacket(InputStream stream, int packetId) {
		if (packetId == 7) {
			session.getChannel().close();
			return;
		}
		if (packetId == 4) {
			session.getEncoder(GrabEncoder.class).setEncryptionValue(stream.readUnsignedByte());
			if (stream.readUnsignedShort() != 0)
				session.getChannel().close();
		} else {
			stream.skip(5);
		}
	}
}
