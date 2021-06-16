package com.rs.cache.loaders.interfaces;

import java.util.Arrays;

public class IFTargetParams {
	
	public enum UseFlag {
		GROUND_ITEM(0x1),
		NPC(0x2), 
		WORLD_OBJECT(0x4),
		PLAYER(0x8),
		SELF(0x10),
		ICOMPONENT(0x20),
		WORLD_TILE(0x40);
		
		private int flag;
		
		private UseFlag(int flag) {
			this.flag = flag;
		}
		
		public int getFlag() {
			return flag;
		}
	}
	
	private int interfaceId;
	private int componentId;
	private int fromSlot;
	private int toSlot;
	private int settings;
	
	public IFTargetParams(int interfaceId, int componentId, int fromSlot, int toSlot, int settings) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.fromSlot = fromSlot;
		this.toSlot = toSlot;
		this.settings =  settings;
	}
	
	public IFTargetParams(int settings, int id) {
		this.settings = settings;
		this.interfaceId = id;
	}
	
	public IFTargetParams(int interfaceId, int componentId, int fromSlot, int toSlot) {
		this(interfaceId, componentId, fromSlot, toSlot, 0);
	}

	public boolean clickOptionEnabled(int i) {
		return 0 != (settings >> 1 + i & 0x1);
	}
	
	public IFTargetParams enableRightClickOptions(int... ids) {
		Arrays.stream(ids).forEach((id) -> enableRightClickOption(id));
		return this;
	}

	public IFTargetParams enableRightClickOption(int id) {
		if (id < 0 || id > 9)
			return null;
		settings &= ~(0x1 << (id + 1));
		settings |= (0x1 << (id + 1));
		return this;
	}

	public final boolean useOptionEnabled(UseFlag flag) {
		return ((settings >> 11 & 0x7F) & flag.getFlag()) != 0;
	}
	
	public IFTargetParams enableUseOptions(UseFlag... flags) {
		Arrays.stream(flags).forEach(this::enableUseOption);
		return this;
	}
	
	public final int getUseOptionFlags() {
		return IFTargetParams.getUseOptionFlags(this.settings);
	}
	
	static final int getUseOptionFlags(int settings) {
		return settings >> 11 & 0x7f;
	}

	
	public IFTargetParams enableUseOption(UseFlag flag) {
		int useOptions = settings >> 11 & 0x7F;
		useOptions |= flag.flag;
		settings &= ~(useOptions << 11);
		settings |= (useOptions << 11);
		return this;
	}

	public boolean dragEnabled() {
		return (settings >> 21 & 0x1) != 0;
	}
	
	public IFTargetParams enableDrag() {
		settings &= ~(1 << 21);
		settings |= (1 << 21);
		return this;
	}

	public boolean continueOptionEnabled() {
		return (settings & 0x1) != 0;
	}
	
	public IFTargetParams enableContinueButton() {
		settings |= 0x1;
		return this;
	}

	public boolean bit23Enabled() {
		return 0 != (settings >> 23 & 0x1);
	}
	
	public IFTargetParams enableBit23() {
		settings &= ~(1 << 23);
		settings |= (1 << 23);
		return this;
	}

	public boolean bit22Enabled() {
		return 0 != (settings >> 22 & 0x1);
	}
	
	public IFTargetParams enableBit22() {
		settings &= ~(1 << 22);
		settings |= (1 << 22);
		return this;
	}
	
	public int getDepth() {
		return settings >> 18 & 0x7;
	}
	
	public IFTargetParams setDepth(int depth) {
		if (depth < 0 || depth > 7)
			return null;
		settings &= ~(0x7 << 18);
		settings |= (depth << 18);
		return this;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}

	public int getComponentId() {
		return componentId;
	}

	public int getFromSlot() {
		return fromSlot;
	}

	public int getToSlot() {
		return toSlot;
	}

	public int getSettings() {
		return settings;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof IFTargetParams)
			return ((IFTargetParams) other).settings == settings;
		return false;
	}
	
	@Override
	public String toString() {
		String s = "player.getPackets().sendIComponentSettings(new IComponentSettings(" + interfaceId + ", " + componentId + ", " + fromSlot + ", " + toSlot + ")";
		String useFlags = "";
		for (int i = 0;i < UseFlag.values().length;i++) {
			useFlags += useOptionEnabled(UseFlag.values()[i]) ? "UseFlag." + UseFlag.values()[i].name() + "," : "";
		}
		
		if (!useFlags.equals("")) {
			s += ".enableUseOptions(" + useFlags.substring(0, useFlags.length()-1) + ")";
		}
		String rightClicks = "";
		for (int i = 0;i <= 9;i++) {
			rightClicks += clickOptionEnabled(i) ? "" + i + "," : "";
		}
		if (!rightClicks.equals("")) {
			s += ".enableRightClickOptions(" + rightClicks.substring(0, rightClicks.length()-1) + ")";
		}
		if (getDepth() != 0) {
			s += ".setDepth(" + getDepth() + ")";
		}
		if (continueOptionEnabled()) {
			s += ".enableContinueButton()";
		}
		if (dragEnabled()) {
			s += ".enableDrag()";
		}
		if (bit23Enabled()) {
			s += ".enableBit23()";
		}
		if (bit22Enabled()) {
			s += ".enableBit22()";
		}
		s += "); //" + settings;
		return s;
	}
}