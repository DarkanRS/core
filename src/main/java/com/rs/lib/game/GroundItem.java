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
package com.rs.lib.game;

public class GroundItem extends Item {

	public enum GroundItemType {
		NORMAL, INVISIBLE, FOREVER
	}

	private WorldTile tile;
	private int visibleToId = 0;
	private String creatorUsername;
	private int creatorId;
	private GroundItemType type;
	private int ticks;
	private int privateTime;
	private int deleteTime;

	public GroundItem(int id) {
		super(id);
	}

	@Override
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public GroundItem(Item item, WorldTile tile, String ownerUsername, GroundItemType type) {
		super(item.getId(), item.getAmount(), item.getMetaData());
		this.tile = tile;
		if (ownerUsername != null) {
			this.creatorUsername = ownerUsername;
			this.visibleToId = this.creatorId = ownerUsername.hashCode();
		}
		this.type = type;
		this.privateTime = -1;
		this.deleteTime = -1;
	}
	
	public GroundItem(Item item, WorldTile tile, GroundItemType type) {
		this(item, tile, null, type);
	}
	
	public GroundItem(Item item, WorldTile tile) {
		this(item, tile, null, GroundItemType.NORMAL);
	}

	public WorldTile getTile() {
		return tile;
	}

	public boolean isInvisible() {
		return type == GroundItemType.INVISIBLE;
	}

	public boolean isRespawn() {
		return type == GroundItemType.FOREVER;
	}
	
	public String getCreatorUsername() {
		return creatorUsername;
	}

	public int getVisibleToId() {
		return visibleToId;
	}

	public boolean isPrivate() {
		return visibleToId != 0;
	}
	
//	public void setInvisible(boolean invisible) {
//		type = invisible ? GroundItemType.INVISIBLE : GroundItemType.NORMAL;
//	}

	@Override
	public String toString() {
		return "["+super.toString()+", " + tile.toString() + (creatorUsername != null ? (" creator: "+creatorUsername) : "") + "]";
	}

	public void removeOwner() {
		visibleToId = 0;
	}
	
	public void tick() {
		ticks++;
	}

	public int getSourceId() {
		return creatorId;
	}

	public int getTicks() {
		return ticks;
	}

	public int getPrivateTime() {
		return privateTime;
	}
	
	public void setPrivateTime(int privateTime) {
		this.privateTime = privateTime;
	}
	
	public void setDeleteTime(int deleteTime) {
		this.deleteTime = deleteTime;
	}

	public int getDeleteTime() {
		return deleteTime;
	}

	public void setHiddenTime(int ticks) {
		privateTime = this.ticks + ticks;
	}
}
