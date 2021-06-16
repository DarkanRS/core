package com.rs.lib.util;

public class ReflectionCheck {
	
	private String className;
	private String modifiers;
	private String returnType;
	private String methodName;
	private String[] params;
	private boolean exists;
	private int id;
	
	public ReflectionCheck(String className, String modifiers, String returnType, String methodName, boolean exists, String... params) {
		this.id = Utils.random(Integer.MAX_VALUE-1);
		this.className = className;
		this.modifiers = modifiers;
		this.returnType = returnType;
		this.methodName = methodName;
		this.exists = exists;
		this.params = params;
	}
	
	public String getClassName() {
		return className;
	}
	public String getReturnType() {
		return returnType;
	}
	public String getMethodName() {
		return methodName;
	}
	public String[] getParams() {
		return params;
	}
	public boolean exists() {
		return exists;
	}

	public int getId() {
		return id;
	}

	public String getModifiers() {
		return modifiers;
	}
}
