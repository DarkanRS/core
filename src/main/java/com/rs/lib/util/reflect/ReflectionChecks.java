package com.rs.lib.util.reflect;

import com.rs.lib.util.Utils;

public class ReflectionChecks {
	
	private int id;
	private ReflectionCheck[] checks;
	
	public ReflectionChecks(ReflectionCheck... checks) {
		this.id = Utils.random(Integer.MAX_VALUE-1);
	}

	public int getId() {
		return id;
	}

	public ReflectionCheck[] getChecks() {
		return checks;
	}

}
