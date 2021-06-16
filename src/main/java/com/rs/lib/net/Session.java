package com.rs.lib.net;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.lib.Constants;
import com.rs.lib.io.IsaacKeyPair;
import com.rs.lib.io.OutputStream;
import com.rs.lib.net.packets.Packet;
import com.rs.lib.net.packets.PacketEncoder;

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
		if (outStream == null || !channel.isConnected())
			return null;
		return channel.write(ChannelBuffers.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset()));
	}

	public final ChannelFuture write(ChannelBuffer outStream) {
		if (outStream == null || !channel.isConnected())
			return null;
		return channel.write(outStream);
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
		return channel == null ? "" : channel.getRemoteAddress().toString().split(":")[0].replace("/", "");
	}

	public String getLocalAddress() {
		return channel.getLocalAddress().toString();
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
