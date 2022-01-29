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
package com.rs.cache.loaders.cs2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;

import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public class CS2Script {
	public String[] stringOpValues;
	public String name;
	public CS2Instruction[] operations;
	public CS2Type[] arguments;
	public int[] operationOpcodes;
	public int[] intOpValues;
	public long[] longOpValues;
	public int longArgsCount;
	public int intLocalsCount;
	public int stringLocalsCount;
	public int intArgsCount;
	public int stringArgsCount;
	public int longLocalsCount;
	public HashMap<Integer, Integer>[] switchMaps;
	public int id;

	public CS2Script(InputStream buffer) {
		int instructionLength = decodeHeader(buffer);
		int opCount = 0;
		while (buffer.getOffset() < instructionLength) {
			CS2Instruction op = getOpcode(buffer);
			decodeInstruction(buffer, opCount, op);
			opCount++;
		}
		postDecode();
	}

	CS2Instruction getOpcode(InputStream buffer) {
		int opcode = buffer.readUnsignedShort();
		if (opcode < 0 || opcode >= CS2Instruction.values().length) {
			throw new RuntimeException("Invalid operation code: " + opcode);
		}
		CS2Instruction op = CS2Instruction.getByOpcode(opcode);
		return op;
	}
	
	public Object getParam(int i) {
		CS2Instruction op = operations[i];
		if (op == CS2Instruction.PUSH_LONG) {
			return longOpValues[i];
		}
		if (op == CS2Instruction.PUSH_STRING) {
			return "\""+stringOpValues[i]+"\"";
		}
		if (op.hasIntConstant()) {
			return intOpValues[i];
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private int decodeHeader(InputStream buffer) {
		buffer.setOffset(buffer.getLength() - 2);
		int switchBlockSize = buffer.readUnsignedShort();
		int instructionLength = buffer.getBuffer().length - 2 - switchBlockSize - 16;
		buffer.setOffset(instructionLength);
		int codeSize = buffer.readInt();
		intLocalsCount = buffer.readUnsignedShort();
		stringLocalsCount = buffer.readUnsignedShort();
		longLocalsCount = buffer.readUnsignedShort();
		intArgsCount = buffer.readUnsignedShort();
		stringArgsCount = buffer.readUnsignedShort();
		longArgsCount = buffer.readUnsignedShort();
		int switchesCount = buffer.readUnsignedByte();
		if (switchesCount > 0) {
			switchMaps = new HashMap[switchesCount];
			for (int i = 0; i < switchesCount; i++) {
				int numCases = buffer.readUnsignedShort();
				switchMaps[i] = new HashMap<Integer, Integer>(numCases);
				while (numCases-- > 0) {
					switchMaps[i].put(buffer.readInt(), buffer.readInt());
				}
			}
		}
		
		buffer.setOffset(0);
		name = buffer.readNullString();
		operations = new CS2Instruction[codeSize];
		operationOpcodes = new int[codeSize];
		return instructionLength;
	}

	private void decodeInstruction(InputStream buffer, int opIndex, CS2Instruction operation) {
		int opLength = operations.length;
		if (operation == CS2Instruction.PUSH_STRING) {
			if (stringOpValues == null)
				stringOpValues = new String[opLength];
			stringOpValues[opIndex] = buffer.readString();
		} else if (CS2Instruction.PUSH_LONG == operation) {
			if (null == longOpValues)
				longOpValues = new long[opLength];
			longOpValues[opIndex] = buffer.readLong();
		} else {
			if (null == intOpValues)
				intOpValues = new int[opLength];
			if (operation.hasIntConstant())
				intOpValues[opIndex] = buffer.readInt();
			else
				intOpValues[opIndex] = buffer.readUnsignedByte();
		}
		operations[opIndex] = operation;
		operationOpcodes[opIndex] = operation.getOpcode();
	}
	
	public void postDecode() {
		arguments = new CS2Type[intArgsCount + stringArgsCount + longArgsCount];
		int write = 0;
		for (int i = 0; i < intArgsCount; i++)
			arguments[write++] = CS2Type.INT;
		for (int i = 0; i < stringArgsCount; i++)
			arguments[write++] = CS2Type.STRING;
		for (int i = 0; i < longArgsCount; i++)
			arguments[write++] = CS2Type.LONG;
	}
	
	public void write(Store store) {
		store.getIndex(IndexType.CS2_SCRIPTS).putArchive(id, encode());
	}
	
	public byte[] encode() {
        OutputStream out = new OutputStream();
        if (name == null) {
            out.writeByte(0);
        } else
            out.writeString(name);
        for (int i = 0; i < operations.length; i++) {
            CS2Instruction op = operations[i];
            if(op == null) continue;
            out.writeShort(op.getOpcode());
            if (op == CS2Instruction.PUSH_STRING) {
                out.writeString((String) stringOpValues[i]);
            } else if (CS2Instruction.PUSH_LONG == op) {
                out.writeLong(longOpValues[i]);
            } else {
                if (op.hasIntConstant()) {
                    out.writeInt(intOpValues[i]);
                } else {
                    out.writeByte(intOpValues[i]);
                }
            }
        }

        out.writeInt(operations.length);
        out.writeShort(intLocalsCount);
        out.writeShort(stringLocalsCount);
        out.writeShort(longLocalsCount);
        out.writeShort(intArgsCount);
        out.writeShort(stringArgsCount);
        out.writeShort(longArgsCount);

        OutputStream switchBlock = new OutputStream();
        if (switchMaps == null) {
            switchBlock.writeByte(0);
        } else {
            switchBlock.writeByte(switchMaps.length);
            if (switchMaps.length > 0) {
                for (int i = 0; i < switchMaps.length; i++) {
                    HashMap<Integer, Integer> map = switchMaps[i];
                    switchBlock.writeShort(map.size());
                    for (int key : map.keySet()) {
                        switchBlock.writeInt(key);
                        switchBlock.writeInt(map.get(key));
                    }
                }
            }
        }

        byte[] switchBytes = switchBlock.toByteArray();
        out.writeBytes(switchBytes);
        out.writeShort(switchBytes.length);
        return out.toByteArray();
    }
	
	public String getIntBytes(int i) {
        return ((byte) (i >> 24))+" "+((byte) (i >> 16))+" "+((byte) (i >> 8))+" "+((byte) i);
    }

    public String getShortBytes(int i) {
        return ((byte) (i >> 8))+" "+((byte) i);
    }
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof CS2Script))
			return false;
		CS2Script script = (CS2Script) other;
		if (script.operationOpcodes != null) {
			if (this.operationOpcodes == null) {
				System.out.println("Mismatching operation opcodes.");
				return false;
			}
			if (!Arrays.equals(script.operationOpcodes, this.operationOpcodes)) {
				System.out.println("Mismatching operation opcodes");
				return false;
			}
		}
		if (script.intOpValues != null) {
			if (this.intOpValues == null) {
				System.out.println("int op values null shouldn't be");
				return false;
			}
			if (!Arrays.equals(script.intOpValues, this.intOpValues)) {
				System.out.println("Mismatching int op values");
				return false;
			}
		}
		if (script.longOpValues != null) {
			if (this.longOpValues == null) {
				System.out.println("long op values null shouldn't be");
				return false;
			}
			if (!Arrays.equals(script.longOpValues, this.longOpValues)) {
				System.out.println("Mismatching long op values");
				return false;
			}
		}
		if (script.stringOpValues != null) {
			if (this.stringOpValues == null) {
				System.out.println("String op values null shouldn't be");
				return false;
			}
			if (!Arrays.equals(script.stringOpValues, this.stringOpValues)) {
				System.out.println("Mismatching string op values");
				System.out.println(Arrays.toString(this.stringOpValues));
				System.out.println(Arrays.toString(script.stringOpValues));
				return false;
			}
		}
		if (script.switchMaps != null) {
			if (this.switchMaps == null) {
				System.out.println("Switchmap null shouldn't be");
				return false;
			}
			if (this.switchMaps.length != script.switchMaps.length) {
				System.out.println("Mismatching switch map lengths");
				return false;
			}
			for (int i = 0;i < this.switchMaps.length;i++) {
				HashMap<Integer, Integer> map1 = this.switchMaps[i];
				HashMap<Integer, Integer> map2 = script.switchMaps[i];
				for (int key : map1.keySet()) {
					if (map2.get(key).intValue() != map1.get(key).intValue()) {
						System.out.println("Mismatching map keys: " + map1.get(key) + " - " + map2.get(key));
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		String newLine = System.getProperty("line.separator");

		result.append(this.getClass().getName());
		result.append(" {");
		result.append(newLine);

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()))
				continue;
			result.append("  ");
			try {
				result.append(field.getType().getCanonicalName() + " " + field.getName() + ": ");
				result.append(Utils.getFieldValue(this, field));
			} catch (Throwable ex) {
				System.out.println(ex);
			}
			result.append(newLine);
		}
		result.append("}");

		return result.toString();
	}

	public String getOpString(int i) {
		String str = "CS2Instruction."+operations[i] + ", //(";
		if (intOpValues != null && (stringOpValues == null || stringOpValues[i] == null)) {
			str += intOpValues[i];
		}
		if (longOpValues != null) {
			str += longOpValues[i];
		}
		if (stringOpValues != null) {
			str += (stringOpValues[i] != null ? stringOpValues[i] : "");
		}
		return str + ")";
	}

	public void setInstruction(int index, CS2Instruction instr, int intParam) {
		operations[index] = instr;
		intOpValues[index] = intParam;
	}
}
