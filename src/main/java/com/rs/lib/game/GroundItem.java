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

	public GroundItem(int id) {
		super(id);
	}

	@Override
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public GroundItem(Item item, WorldTile tile, String creator, GroundItemType type) {
		super(item.getId(), item.getAmount(), item.getMetaData());
		this.tile = tile;
		if (creator != null) {
			this.creatorUsername = creator;
			this.visibleToId = this.creatorId = creator.hashCode();
		}
		this.type = type;
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

	@Override
	public String toString() {
		return "["+super.toString()+", " + tile.toString() + (creatorUsername != null ? (" creator: "+creatorUsername) : "") + "]";
	}

	public void removeOwner() {
		visibleToId = 0;
	}

	public int getSourceId() {
		return creatorId;
	}
}
