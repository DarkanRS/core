package com.rs.lib.util;

import java.util.concurrent.ConcurrentHashMap;

public class GenericAttribMap {
	private ConcurrentHashMap<String, Object> attribs;
	
	public GenericAttribMap() {
		this.attribs = new ConcurrentHashMap<>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T setO(String name, Object value) {
		if (value == null) {
			Object old = attribs.remove(name);
			return old == null ? null : (T) old;
		}
		Object old = attribs.put("O"+name, value);
		return old == null ? null : (T) old;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getO(String name) {
		if (attribs.get("O"+name) == null)
			return null;
		return (T) attribs.get("O"+name);
	}
	
	public void setI(String name, int value) {
		attribs.put("I"+name, value);
	}
	
	public void setD(String name, double value) {
		attribs.put("D"+name, value);
	}
	
	public void setB(String name, boolean value) {
		attribs.put("B"+name, value);
	}
	
	public void setL(String name, long value) {
		attribs.put("L"+name, value);
	}
	
	public boolean getB(String name) {
		if (attribs.get("B"+name) == null)
			return false;
		return (Boolean) attribs.get("B"+name);
	}
	
	public int getI(String name, int def) {
		Object val = attribs.get("I"+name);
		if (val == null)
			return def;
		return (int) (val instanceof Integer ? (int) val : (double) val);
	}
	
	public int getI(String name) {
		return getI(name, 0);
	}
	
	public double getD(String name, double def) {
		if (attribs.get("D"+name) == null)
			return def;
		return (Double) attribs.get("D"+name);
	}
	
	public double getD(String name) {
		return getD(name, 0.0);
	}
	
	public long getL(String name) {
		if (attribs.get("L"+name) == null)
			return 0;
		return (Long) attribs.get("L"+name);
	}
	
	public void incI(String name) {
		int newVal = getI(name) + 1;
		setI(name, newVal);
	}

	public void clear() {
		attribs.clear();
	}

	public int removeI(String name) {
		int i = getI(name);
		attribs.remove("I"+name);
		return i;
	}

	public boolean removeB(String name) {
		boolean b = getB(name);
		attribs.remove("B"+name);
		return b;
	}
	
	public <T> T removeO(String name) {
		T o = getO(name);
		attribs.remove("O"+name);
		return o;
	}

	public long removeL(String name) {
		long l = getL(name);
		attribs.remove("L"+name);
		return l;
	}

	public int removeI(String name, int defaultVal) {
		int i = getI(name, defaultVal);
		attribs.remove("I"+name);
		return i;
	}
}
