package com.rs.lib.net.packets.encoders.social;

import com.rs.lib.io.OutputStream;
import com.rs.lib.model.Account;
import com.rs.lib.model.Friend;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.packets.PacketEncoder;

public class FriendStatus extends PacketEncoder {
	
	private Account base;
	private Friend[] friends;

	public FriendStatus(Account base, Friend... friends) {
		super(ServerPacket.FRIEND_STATUS);
		this.base = base;
		this.friends = friends;
	}

	@Override
	public void encodeBody(OutputStream stream) {
		for (Friend friend : friends)
			encodePlayer(base, friend, stream);
	}
	
	public void encodePlayer(Account player, Friend other, OutputStream stream) {
		boolean online = false;
		if (!other.isOffline() && other.getAccount().getSocial().onlineTo(player) && player.getSocial().onlineTo(other.getAccount()))
			online = true;
		stream.writeByte(0); //idk, was called warnMessage in matrix?
		stream.writeString(other.getAccount().getDisplayName());
		stream.writeString(other.getAccount().getSocial() == null ? "Player not found" : ""); //previous name
		stream.writeShort(online ? other.getWorld() != null ? other.getWorld().getNumber() : 1100 : 0);
		stream.writeByte(player.getSocial().getFriendsChat().getRank(other.getAccount().getUsername()));
		stream.writeByte(0);
		if (online) {
			stream.writeString(other.getWorld() != null ? "World " + other.getWorld().getNumber() : "Lobby");
			stream.writeByte(other.getWorld() != null ? other.getWorld().getNumber() : 1100);
			stream.writeInt(0);
		}
	}
}
