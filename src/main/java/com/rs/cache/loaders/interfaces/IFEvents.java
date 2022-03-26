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
package com.rs.cache.loaders.interfaces;

import java.util.Arrays;

public class IFEvents {
	
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
	
	public IFEvents(int interfaceId, int componentId, int fromSlot, int toSlot, int settings) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.fromSlot = fromSlot;
		this.toSlot = toSlot;
		this.settings =  settings;
	}
	
	public IFEvents(int settings, int interfaceId) {
		this.settings = settings;
		this.interfaceId = interfaceId;
	}
	
	public IFEvents(int interfaceId, int componentId, int fromSlot, int toSlot) {
		this(interfaceId, componentId, fromSlot, toSlot, 0);
	}

	public boolean clickOptionEnabled(int i) {
		return 0 != (settings >> 1 + i & 0x1);
	}
	
	public IFEvents enableRightClickOptions(int... ids) {
		Arrays.stream(ids).forEach((id) -> enableRightClickOption(id));
		return this;
	}

	public IFEvents enableRightClickOption(int id) {
		if (id < 0 || id > 9)
			return null;
		settings &= ~(0x1 << (id + 1));
		settings |= (0x1 << (id + 1));
		return this;
	}

	public final boolean useOptionEnabled(UseFlag flag) {
		return ((settings >> 11 & 0x7F) & flag.getFlag()) != 0;
	}
	
	public IFEvents enableUseOptions(UseFlag... flags) {
		Arrays.stream(flags).forEach(this::enableUseOption);
		return this;
	}
	
	public final int getUseOptionFlags() {
		return IFEvents.getUseOptionFlags(this.settings);
	}
	
	static final int getUseOptionFlags(int settings) {
		return settings >> 11 & 0x7f;
	}

	
	public IFEvents enableUseOption(UseFlag flag) {
		int useOptions = settings >> 11 & 0x7F;
		useOptions |= flag.flag;
		settings &= ~(useOptions << 11);
		settings |= (useOptions << 11);
		return this;
	}

	public boolean dragEnabled() {
		return (settings >> 21 & 0x1) != 0;
	}
	
	public IFEvents enableDrag() {
		settings &= ~(1 << 21);
		settings |= (1 << 21);
		return this;
	}

	public boolean continueOptionEnabled() {
		return (settings & 0x1) != 0;
	}
	
	public IFEvents enableContinueButton() {
		settings |= 0x1;
		return this;
	}

	public boolean ignoresDepthFlags() {
		return 0 != (settings >> 23 & 0x1);
	}
	
	public IFEvents enableDepthFlagIgnoring() {
		settings &= ~(1 << 23);
		settings |= (1 << 23);
		return this;
	}

	public boolean isTargetableByUse() {
		return 0 != (settings >> 22 & 0x1);
	}
	
	public IFEvents enableUseTargetability() {
		settings &= ~(1 << 22);
		settings |= (1 << 22);
		return this;
	}
	
	public int getDepth() {
		return settings >> 18 & 0x7;
	}
	
	public IFEvents setDepth(int depth) {
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
		if (other instanceof IFEvents)
			return ((IFEvents) other).settings == settings;
		return false;
	}
	
	@Override
	public String toString() {
		String s = "player.getPackets().setIFEvents(new IFEvents(" + interfaceId + ", " + componentId + ", " + fromSlot + ", " + toSlot + ")";
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
		if (ignoresDepthFlags()) {
			s += ".enableDepthFlagIgnoring()";
		}
		if (isTargetableByUse()) {
			s += ".enableUseTargetability()";
		}
		s += "); //" + settings;
		return s;
	}
}