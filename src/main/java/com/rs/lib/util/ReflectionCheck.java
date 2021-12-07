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
