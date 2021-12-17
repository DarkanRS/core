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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.loaders.VarBitDefinitions;
import com.rs.lib.net.ServerPacket;
import com.rs.lib.net.Session;
import com.rs.lib.net.packets.encoders.vars.Varp;

public class VarManager {

	public static final int[] BIT_MASKS = new int[32];

	static {
		int i = 2;
		for (int i2 = 0; i2 < 32; i2++) {
			BIT_MASKS[i2] = i - 1;
			i += i;
		}
	}

	private transient HashSet<Integer> modified;
	private transient final Object lock = new Object();
	private transient int[] values;
	private transient Session session;
	private Map<Integer, Integer> vars;

	public VarManager() {
		values = new int[Cache.STORE.getIndex(IndexType.CONFIG).getLastFileId(ArchiveType.VARS.getId()) + 1];
		modified = new HashSet<>();
	}
	
	public void setSession(Session session) {
		this.session = session;
		if (vars == null)
			vars = new HashMap<>();
		for (int varId : vars.keySet())
			setVar(varId, vars.get(varId));
	}
	
	public void setVar(int id, int value, boolean forceSend, boolean save) {
		synchronized(lock) {
			if (forceSend)
				modified.add(id);
			if (id < 0 || id >= values.length)
				return;
			if (values[id] == value)
				return;
			values[id] = value;
			if (save)
				vars.put(id, value);
			modified.add(id);
		}
	}
	
	public void setVar(int id, int value, boolean forceSend) {
		setVar(id, value, forceSend, false);
	}
	
	public void setVar(int id, int value) {
		setVar(id, value, false, false);
	}
	
	public void saveVar(int id, int value) {
		setVar(id, value, false, true);
	}
	
	public void setVarBit(int id, int value, boolean forceSend, boolean save) {
		VarBitDefinitions defs = VarBitDefinitions.getDefs(id);
		int mask = BIT_MASKS[defs.endBit - defs.startBit];
		if (value < 0 || value > mask) {
			value = 0;
		}
		mask <<= defs.startBit;
		int varpValue = (values[defs.baseVar] & (mask ^ 0xffffffff) | value << defs.startBit & mask);
		if (varpValue != values[defs.baseVar]) {
			setVar(defs.baseVar, varpValue, forceSend, save);
		}
	}
	
	public void setVarBit(int id, int value, boolean forceSend) {
		setVarBit(id, value, forceSend, false);
	}
	
	public void setVarBit(int id, int value) {
		setVarBit(id, value, false, false);
	}
	
	public void saveVarBit(int id, int value) {
		setVarBit(id, value, false, true);
	}

	public int getVar(int id) {
		return values[id];
	}
	
	public int getVarBit(int id) {
		VarBitDefinitions defs = VarBitDefinitions.getDefs(id);
		return values[defs.baseVar] >> defs.startBit & BIT_MASKS[defs.endBit - defs.startBit];
	}
	
	public boolean bitFlagged(int id, int bit) {
		return (values[id] & (1 << bit)) != 0;
	}

	public void syncVarsToClient() {
		synchronized(lock) {
			for (int id : modified) {
				session.writeToQueue(new Varp(id, values[id]));
			}
			modified.clear();
		}
	}
	
	public void clearVars() {
		for (int i = 0;i < values.length;i++) {
			values[i] = 0;
		}
		session.writeToQueue(ServerPacket.CLEAR_VARPS);
	}
}
