package com.rs.lib.web.dto;

import com.rs.lib.net.packets.PacketEncoder;

public record PacketEncoderDto(String username, PacketEncoder... encoders) { }
