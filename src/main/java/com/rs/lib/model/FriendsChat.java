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
package com.rs.lib.model;

import java.util.HashMap;
import java.util.Map;

import com.rs.lib.util.Utils;

public class FriendsChat {
	private String name;
	private Map<String, Rank> friendsChatRanks;
	private Rank rankToEnter = Rank.UNRANKED;
	private Rank rankToSpeak = Rank.UNRANKED;
	private Rank rankToKick = Rank.OWNER;
	private Rank rankToLS = Rank.FRIEND;
	private boolean coinshare;
	
	public enum Rank {
		UNRANKED(-1),
		FRIEND(0),
		RECRUIT(1),
		CORPORAL(2),
		SERGEANT(3),
		LIEUTENANT(4),
		CAPTAIN(5),
		GENERAL(6),
		OWNER(7),
		JMOD(127);
		
		private static Map<Integer, Rank> MAP = new HashMap<>();
		
		static {
			for (Rank r : Rank.values())
				MAP.put(r.getId(), r);
		}
		
		public static Rank forId(int id) {
			return MAP.get(id);
		}
		
		private int id;
		
		private Rank(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
	}
	
	public FriendsChat() {
		friendsChatRanks = new HashMap<>(200);
		rankToKick = Rank.OWNER;
		rankToLS = Rank.UNRANKED;
	}
	
	public Rank getRank(String username) {
		Rank rank = getFriendsChatRanks().get(username);
		if (rank == null)
			return Rank.UNRANKED;
		return rank;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Rank> getFriendsChatRanks() {
		return friendsChatRanks;
	}

	public void setFriendsChatRanks(Map<String, Rank> friendsChatRanks) {
		this.friendsChatRanks = friendsChatRanks;
	}

	public Rank getRankToEnter() {
		return rankToEnter;
	}

	public void setRankToEnter(Rank rankToEnter) {
		this.rankToEnter = rankToEnter;
	}

	public Rank getRankToSpeak() {
		return rankToSpeak;
	}

	public void setRankToSpeak(Rank rankToSpeak) {
		this.rankToSpeak = rankToSpeak;
	}

	public Rank getRankToKick() {
		return rankToKick;
	}

	public void setRankToKick(Rank rankToKick) {
		this.rankToKick = rankToKick;
	}

	public Rank getRankToLS() {
		return rankToLS;
	}

	public void setRankToLS(Rank rankToLS) {
		this.rankToLS = rankToLS;
	}

	public boolean isCoinshare() {
		return coinshare;
	}

	public void setCoinshare(boolean coinshare) {
		this.coinshare = coinshare;
	}
	
	public void setRank(String name, Rank rank) {
		if (rank == null) {
			getFriendsChatRanks().remove(name);
			return;
		}
		getFriendsChatRanks().put(Utils.formatPlayerNameForProtocol(name), rank);
	}
}
