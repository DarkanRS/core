package com.rs.lib.net.packets;

public interface PacketHandler<T, K extends Packet> {
	void handle(T player, K packet);
}
