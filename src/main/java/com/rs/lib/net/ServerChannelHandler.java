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

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import com.rs.lib.Globals;
import com.rs.lib.io.InputStream;
import com.rs.lib.net.decoders.GameDecoder;
import com.rs.lib.util.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

@Sharable
public final class ServerChannelHandler extends ChannelInboundHandlerAdapter {

	private static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");
	private static ChannelGroup CHANNELS;
	private static ServerBootstrap BOOTSTRAP;
	private static EventLoopGroup BOSS_GROUP;
	private static EventLoopGroup WORKER_GROUP;
	private Class<? extends Decoder> baseDecoderClass;

	public static final void init(int port, Class<? extends Decoder> baseDecoderClass) {
		new ServerChannelHandler(port, baseDecoderClass);
	}

	public static int getConnectedChannelsSize() {
		return CHANNELS == null ? 0 : CHANNELS.size();
	}

	private ServerChannelHandler(int port, Class<? extends Decoder> baseDecoderClass) {
		this.baseDecoderClass = baseDecoderClass;
		BOSS_GROUP = new NioEventLoopGroup(1);
		WORKER_GROUP = new NioEventLoopGroup();
		CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		BOOTSTRAP = new ServerBootstrap();
		BOOTSTRAP.group(BOSS_GROUP, WORKER_GROUP)
		.channel(NioServerSocketChannel.class)
		.childHandler(this)
		.option(ChannelOption.SO_REUSEADDR, true)
		.childOption(ChannelOption.TCP_NODELAY, true)
		.childOption(ChannelOption.SO_KEEPALIVE, true)
		.bind(new InetSocketAddress(port));
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		CHANNELS.add(ctx.channel());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) {
		CHANNELS.remove(ctx.channel());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		try {
			ctx.channel().attr(SESSION_KEY).set(new Session(ctx.channel(), baseDecoderClass.getConstructor().newInstance()));
			if (Globals.DEBUG)
				System.out.println("Connection from " + ctx.channel().remoteAddress());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
			Logger.handle(e1);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		if (Globals.DEBUG)
			System.out.println("Connection disconnected " + ctx.channel().remoteAddress());
		Session session = ctx.channel().attr(SESSION_KEY).get();
		if (session != null) {
			if (session.getDecoder() == null)
				return;
			if (session.getDecoder() instanceof GameDecoder)
				session.getChannel().close(); //TODO this might be messing with attempting to reestablish
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (!(msg instanceof ByteBuf))
			return;
		ByteBuf buf = (ByteBuf) msg;
		Session session = ctx.channel().attr(SESSION_KEY).get();
		if (session != null) {
			if (session.getDecoder() == null)
				return;
			
			byte[] b = new byte[(session.buffer.length - session.bufferOffset) + buf.readableBytes()];
			if ((session.buffer.length - session.bufferOffset) > 0)
				System.arraycopy(session.buffer, session.bufferOffset, b, 0, session.buffer.length - session.bufferOffset);
			buf.readBytes(b, session.buffer.length - session.bufferOffset, b.length - (session.buffer.length - session.bufferOffset));
			buf.release();

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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//Logger.handle(cause);
	}

	public static final void shutdown() {
		CHANNELS.close().awaitUninterruptibly();
		BOSS_GROUP.shutdownGracefully();
		WORKER_GROUP.shutdownGracefully();
	}

}
