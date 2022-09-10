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
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.lib.model.clan;

public enum ClanPermission {
	TALK_IN_CC(1571) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> rank.ordinal() >= clan.getCcChatRank().ordinal();
			};
		}
	},
	KICK_FROM_CC(1570) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> rank.ordinal() >= clan.getCcKickRank().ordinal();
			};
		}
	},
	INVITE(1576) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_INVITE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	LOCK_KEEP(1572) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_LOCK_KEEP.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	ALLOWED_IN_KEEP(1573) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_ENTER_KEEP.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	LOCK_CITADEL(1574) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_LOCK_CITADEL.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	ALLOWED_IN_CITADEL(1575) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_ENTER_CITADEL.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	START_BATTLE(1577) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_START_BATTLE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	LEAD_CLANWARS(1578) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_LEAD_CLANWARS.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	CALL_VOTE(1579) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_CALL_VOTE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	BEGIN_MEETING(1580) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_BEGIN_MEETING.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	PARTY_ROOM(1581) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_PARTY_ROOM.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	THEATER(1582) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_THEATER.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	NOTICE(1583) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_NOTICE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	SIGNPOST(1584) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_SIGNPOST.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	EDIT_BATTLEFIELD(1585) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			case RECRUIT, CORPORAL, SERGEANT, LIEUTENANT, CAPTAIN, GENERAL -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_EDIT_BATTLEFIELD.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			case ADMIN, ORGANIZER, COORDINATOR, OVERSEER, DEPUTY_OWNER -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_EDIT_BATTLEFIELD.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	CITADEL_UPGRADE(1586) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_CITADEL_UPGRADE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	CITADEL_DOWNGRADE(1587) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_CITADEL_DOWNGRADE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	RESOURCE_TRANSFER(1588) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_RESOURCE_TRANSFER.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	RESOURCE_GATHER_GOALS(1589) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_RESOURCE_GATHER.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	BUILD_TIME(1590) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.OVERSEER.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.OVERSEER.ordinal();
				int permissionBase = ClanSetting.OVERSEER_PERMISSION_BUILD_TIME.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	CITADEL_LANGUAGE(1649) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.OVERSEER.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.OVERSEER.ordinal();
				int permissionBase = ClanSetting.OVERSEER_PERMISSION_CITADEL_LANGUAGE.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	LOCK_PLOTS(1792) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			if (rank.ordinal() < ClanRank.ADMIN.ordinal())
				return false;
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.ADMIN.ordinal();
				int permissionBase = ClanSetting.ADMIN_PERMISSION_LOCK_PLOTS.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	},
	CHECK_RESOURCES(1793) {
		@Override
		boolean checkPermission(Clan clan, ClanRank rank) {
			return switch(rank) {
			case NONE -> false;
			case OWNER, JMOD -> true;
			default -> {
				int offset = rank.ordinal() - ClanRank.RECRUIT.ordinal();
				int permissionBase = ClanSetting.RECRUIT_PERMISSION_CHECK_RESOURCES.ordinal();
				yield (int) clan.getSetting(ClanSetting.values()[permissionBase+offset]) == 1;
			}
			};
		}
	};
	
	private int varc;
	
	ClanPermission(int varc) {
		this.varc = varc;
	}
	
	public int getVarc() {
		return varc;
	}

	public boolean hasPermission(Clan clan, ClanRank rank) {
		if (rank == ClanRank.OWNER)
			return true;
		return checkPermission(clan, rank);
	}
	
	abstract boolean checkPermission(Clan clan, ClanRank rank);
}
