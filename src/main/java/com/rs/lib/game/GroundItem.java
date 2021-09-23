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
