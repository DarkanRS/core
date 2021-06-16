package com.rs.lib.net;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.rs.lib.Globals;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.decoders.GameDecoder;
import com.rs.lib.thread.DecoderThreadFactory;
import com.rs.lib.util.Logger;

public final class ServerChannelHandler extends SimpleChannelHandler {

	private static ChannelGroup CHANNELS;
	private static ServerBootstrap BOOTSTRAP;
	private Class<? extends Decoder> baseDecoderClass;

	public static final void init(int port, Class<? extends Decoder> baseDecoderClass) {
		new ServerChannelHandler(port, baseDecoderClass);
	}

	public static int getConnectedChannelsSize() {
		return CHANNELS == null ? 0 : CHANNELS.size();
	}

	private ServerChannelHandler(int port, Class<? extends Decoder> baseDecoderClass) {
		this.baseDecoderClass = baseDecoderClass;
		CHANNELS = new DefaultChannelGroup();
		BOOTSTRAP = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newSingleThreadExecutor(new DecoderThreadFactory()), Executors.newSingleThreadExecutor(new DecoderThreadFactory()), 1));
		BOOTSTRAP.getPipeline().addLast("handler", this);
		BOOTSTRAP.setOption("reuseAddress", true); // reuses adress for bind
		BOOTSTRAP.setOption("child.tcpNoDelay", true);
		BOOTSTRAP.setOption("child.TcpAckFrequency", true);
		BOOTSTRAP.setOption("child.keepAlive", true);
		BOOTSTRAP.bind(new InetSocketAddress(port));
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		CHANNELS.add(e.getChannel());
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		CHANNELS.remove(e.getChannel());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			ctx.setAttachment(new Session(e.getChannel(), baseDecoderClass.getConstructor().newInstance()));
			if (Globals.DEBUG)
				System.out.println("Connection from " + e.getChannel().getRemoteAddress());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			Logger.handle(e1);
		}
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (Globals.DEBUG)
			System.out.println("Connection disconnected " + e.getChannel().getRemoteAddress());
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;
			if (session.getDecoder() == null)
				return;
			if (session.getDecoder() instanceof GameDecoder)
				session.getChannel().close();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!(e.getMessage() instanceof ChannelBuffer)) {
			return;
		}
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		Object sessionObject = ctx.getAttachment();
		if (sessionObject != null && sessionObject instanceof Session) {
			Session session = (Session) sessionObject;

			if (session.getDecoder() == null) {
				return;
			}

			byte[] b = new byte[(session.buffer.length - session.bufferOffset) + buf.readableBytes()];
			if ((session.buffer.length - session.bufferOffset) > 0)
				System.arraycopy(session.buffer, session.bufferOffset, b, 0, session.buffer.length - session.bufferOffset);
			buf.readBytes(b, session.buffer.length - session.bufferOffset, b.length - (session.buffer.length - session.bufferOffset));

			session.buffer = b;
			session.bufferOffset = 0;

			try {
				InputStream is = new InputStream(b);
				session.bufferOffset = session.getDecoder()._decode(is);
				if (session.bufferOffset < 0) { // drop
					session.buffer = new byte[0];
					session.bufferOffset = 0;
				}
			} catch (Throwable er) {
				Logger.handle(er);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ee) throws Exception {
		
	}

	public static final void shutdown() {
		CHANNELS.close().awaitUninterruptibly();
		BOOTSTRAP.releaseExternalResources();
	}

}
