package com.rs.lib.web.dto;

import com.rs.lib.net.packets.Packet;

public record PacketDto(String username, Packet... packets) { }
