package com.rs.lib.game;

import java.util.HashMap;
import java.util.Map;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.lib.util.Utils;

public class Item {

	private transient int slot;
	private short id;
	protected int amount;
	private Map<String, Object> metaData;

	public int getId() {
		return id;
	}

	@Override
	public Item clone() {
		return new Item(id, amount, Utils.cloneMap(metaData));
	}
	
	public Item(Item item) {
		this(item.getId(), item.getAmount(), Utils.cloneMap(item.getMetaData()));
	}

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, Map<String, Object> metaData) {
		this(id, amount);
		this.metaData = metaData;
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public ItemDefinitions getDefinitions() {
		return ItemDefinitions.getDefs(id);
	}

	public int getEquipId() {
		return getDefinitions().getEquipId();
	}

	public Item setAmount(int amount) {
		if (amount < 0 || amount > Integer.MAX_VALUE)
			return this;
		this.amount = amount;
		return this;
	}

	public void setId(int id) {
		this.id = (short) id;
	}

	public int getAmount() {
		return amount;
	}

	public String getName() {
		return getDefinitions().getName();
	}
	
	public int getSlot() {
		return slot;
	}

	public Item setSlot(int slot) {
		this.slot = slot;
		return this;
	}
	
	public Item addMetaData(String key, Object value) {
		if (metaData == null)
			metaData = new HashMap<String, Object>();
		metaData.put(key, value);
		return this;
	}

	public Map<String, Object> getMetaData() {
		return metaData;
	}
	
	public Object getMetaData(String key) {
		if (metaData != null)
			return metaData.get(key);
		return null;
	}
	
	public int incMetaDataI(String key) {
		int val = getMetaDataI(key) + 1;
		addMetaData(key, val);
		return val;
	}
	
	public int decMetaDataI(String key) {
		int val = getMetaDataI(key) - 1;
		addMetaData(key, val);
		return val;
	}
	
	public int getMetaDataI(String key) {
		return getMetaDataI(key, -1);
	}
	
	public int getMetaDataI(String key, int defaultVal) {
		if (metaData != null && metaData.get(key) != null) {
			if (metaData.get(key) instanceof Integer value)
				return value;
			return (int) Math.floor(((double) metaData.get(key)));
		}
		return defaultVal;
	}

	public void deleteMetaData() {
		this.metaData = null;
	}
	
	@Override
	public String toString() {
		return "[" + ItemDefinitions.getDefs(id).name + " ("+id+"), " + amount + "]";
	}

	public boolean containsMetaData() {
		return metaData != null;
	}

	public double getMetaDataD(String key, double defaultVal) {
		if (metaData != null) {
			if (metaData.get(key) != null);
				return (double) metaData.get(key);
		}
		return defaultVal;
	}
	
	public double getMetaDataD(String key) {
		return getMetaDataD(key, 0);
	}
}
