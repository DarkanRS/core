package com.rs.lib.model;

public enum ClanRank {
	NONE(-1),
	RECRUIT(0),
	CORPORAL(1),
	SERGEANT(2),
	LIEUTENANT(3),
	CAPTAIN(4),
	ADMIN(100),
	GENERAL(5),
	ORGANIZER(101),
	COORDINATOR(102),
	OVERSEER(103),
	DEPUTY_OWNER(125),
	OWNER(126);
	
	public static ClanRank forId(int rank) {
		for (ClanRank r : ClanRank.values())
			if (r.iconId == rank)
				return r;
		return RECRUIT;
	}
	
	private final int iconId;
	
	private ClanRank(int iconId) {
		this.iconId = iconId;
	}

	public int getIconId() {
		return iconId;
	}
}
