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
package com.rs.lib.net.encoders;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.lib.Constants;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.Encoder;
import com.rs.lib.net.Session;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public final class GrabEncoder extends Encoder {

	private static byte[] CHECKSUM_CONTAINER;

	private int encryptionValue;

	public GrabEncoder(Session connection) {
		super(connection);
	}

	public final void sendOutdatedClientPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(6);
		ChannelFuture future = session.write(stream);
		if (future != null)
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
	}

	public final void sendOutOfDateClientVersionPacket() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(25);
		ChannelFuture future = session.write(stream);
		if (future != null)
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
	}

	public final void sendStartUpPacket() {
		OutputStream stream = new OutputStream(1 + Constants.GRAB_SERVER_KEYS.length * 4);
		stream.writeByte(0);
		for (int key : Constants.GRAB_SERVER_KEYS)
			stream.writeInt(key);
		session.write(stream);
	}

	public final void sendCacheArchive(int indexId, int containerId, boolean priority) {
		if (indexId == 255 && containerId == 255) {
			if (CHECKSUM_CONTAINER == null)
				CHECKSUM_CONTAINER = Cache.STORE.getChecksumContainer(Constants.RSA_PRIVATE_EXPONENT, Constants.RSA_PRIVATE_MODULUS);
			session.write(new OutputStream(CHECKSUM_CONTAINER));
		} else {
			session.write(getArchivePacketData(indexId, containerId, priority));
		}
	}

	public final ByteBuf getArchivePacketData(int indexId, int archiveId, boolean priority) {
		try {
			if (indexId != 255) {
				if (archiveId > Cache.STORE.getIndices()[indexId].getMainFile().getArchivesCount())
					return null;
				if (indexId > Cache.STORE.getIndices().length)
					return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] archive = indexId == 255 ? Cache.STORE.getIndex255().getArchiveData(archiveId) : Cache.STORE.getIndices()[indexId].getMainFile().getArchiveData(archiveId);
		if (archive == null)
			return null;
		int compression = archive[0] & 0xff;
		int length = ((archive[1] & 0xff) << 24) + ((archive[2] & 0xff) << 16) + ((archive[3] & 0xff) << 8) + (archive[4] & 0xff);
		int settings = compression;
		if (!priority)
			settings |= 0x80;
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeByte(indexId);
		buffer.writeInt(archiveId);
		buffer.writeByte(settings);
		buffer.writeInt(length);
		int realLength = compression != 0 ? length + 4 : length;
		for (int index = 5; index < realLength + 5; index++) {
			if (buffer.writerIndex() % 512 == 0) {
				buffer.writeByte(255);
			}
			buffer.writeByte(archive[index]);
		}
		int v = encryptionValue;
		if (v != 0) {
			for (int i = 0; i < buffer.arrayOffset(); i++)
				buffer.setByte(i, buffer.getByte(i) ^ v);
		}
		return buffer;
	}

	public void setEncryptionValue(int encryptionValue) {
		this.encryptionValue = encryptionValue;
	}
}
