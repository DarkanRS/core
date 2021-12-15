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
package com.rs.lib.net;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.rs.lib.Constants;
import com.rs.lib.io.IsaacKeyPair;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class Session {

	private Channel channel;
	private Decoder decoder;
	private Encoder encoder;
	private long lastPacket = System.currentTimeMillis();
	private IsaacKeyPair isaac;
	
	private transient OutputStream queuedStream;
	private final transient Object streamLock = new Object();
	private transient Queue<Packet> packetQueue;
		
    protected byte[] buffer = new byte[0];
    protected int bufferOffset = 0;

	public Session(Channel channel, Decoder defaultDecoder) {
		this.channel = channel;
		this.packetQueue = new ConcurrentLinkedQueue<Packet>();
		this.queuedStream = new OutputStream();
		if (channel == null)
			return;
		defaultDecoder.setSession(this);
		setDecoder(defaultDecoder);
	}
	
	public final void sendGrabStartup() {
		OutputStream stream = new OutputStream(1 + Constants.GRAB_SERVER_KEYS.length * 4);
		stream.writeByte(0);
		for (int key : Constants.GRAB_SERVER_KEYS)
			stream.writeInt(key);
		write(stream);
	}
	
	public final void sendLoginStartup() {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(0);
		write(stream);
	}
	
	public final void sendClientPacket(int opcode) {
		OutputStream stream = new OutputStream(1);
		stream.writeByte(opcode);
		ChannelFuture future = write(stream);
		if (future != null) {
			future.addListener(ChannelFutureListener.CLOSE);
		} else {
			getChannel().close();
		}
	}
	
	public void writeToQueue(PacketEncoder... encoders) {
		synchronized(streamLock) {
			for (PacketEncoder enc : encoders)
				enc.writeToStream(queuedStream, this);
		}
	}
	
	public void writeToQueue(ServerPacket packet) {
		synchronized(streamLock) {
			if (packet.size != 0)
				throw new Error("Cannot write empty packet for a packet that isn't meant to be empty.");
			queuedStream.writePacket(isaac, packet.opcode, true);
		}
	}
	
	public ChannelFuture flush() {
		synchronized(streamLock) {
			ChannelFuture future = write(queuedStream);
			queuedStream = new OutputStream();
			return future;
		}
	}
	
	public void write(PacketEncoder... encoders) {
		synchronized(streamLock) {
			for (PacketEncoder enc : encoders)
				enc.writeToStream(queuedStream, this);
			flush();
		}
	}
	
	public void write(ServerPacket packet) {
		synchronized(streamLock) {
			if (packet.size != 0)
				throw new Error("Cannot write empty packet for a packet that isn't meant to be empty.");
			queuedStream.writePacket(isaac, packet.opcode, true);
			flush();
		}
	}
	
	public void writeNoIsaac(PacketEncoder encoder) {
		synchronized(streamLock) {
			encoder.writeToStream(queuedStream, null);
			flush();
		}
	}

	public final ChannelFuture write(OutputStream outStream) {
		if (outStream == null || !channel.isActive())
			return null;
		return channel.writeAndFlush(Unpooled.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset()));
	}

	public final ChannelFuture write(ByteBuf outStream) {
		if (outStream == null || !channel.isActive())
			return null;
		return channel.writeAndFlush(outStream);
	}

	public final Channel getChannel() {
		return channel;
	}
	
	public final Decoder getDecoder() {
		return decoder;
	}

	@SuppressWarnings("unchecked")
	public final <T extends Decoder> T getDecoder(Class<T> clazz) {
		if (decoder != null && decoder.getClass().isAssignableFrom(clazz))
			return (T) decoder;
		return null;
	}

	@SuppressWarnings("unchecked")
	public final <T extends Encoder> T getEncoder(Class<T> clazz) {
		if (encoder != null && encoder.getClass().isAssignableFrom(clazz))
			return (T) encoder;
		return null;
	}

	public final void setDecoder(Decoder decoder) {
		packetQueue.clear();
		this.decoder = decoder;
	}
	
	public final void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	public String getIP() {
		return channel == null ? "" : channel.remoteAddress().toString().split(":")[0].replace("/", "");
	}

	public String getLocalAddress() {
		return channel.localAddress().toString();
	}
	
	public void refreshLastPacket() {
		this.lastPacket = System.currentTimeMillis();
	}
	
	public boolean isClosed() {
		return System.currentTimeMillis() - this.lastPacket > 10000;
	}

	public IsaacKeyPair getIsaac() {
		return isaac;
	}

	public void setIsaac(IsaacKeyPair isaac) {
		this.isaac = isaac;
	}
	
	public void queuePacket(Packet packet) {
		if (packetQueue.size() > 100)
			packetQueue.poll();
		packetQueue.add(packet);
	}
	
	public Queue<Packet> getPacketQueue() {
		return packetQueue;
	}

	public void setPacketQueue(Queue<Packet> packetQueue) {
		this.packetQueue = packetQueue;
	}
}
