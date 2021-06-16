package com.rs.lib.net;

import com.rs.lib.io.InputStream;

public abstract class Decoder {

	protected Session session;
	
	public Decoder() {
		
	}

	public Decoder(Session session) {
		this.session = session;
	}
	
	public final int _decode(InputStream stream) {
		session.refreshLastPacket();
		return decode(stream);
	}

	public abstract int decode(InputStream stream);

	protected void setSession(Session session) {
		this.session = session;
	}

}
