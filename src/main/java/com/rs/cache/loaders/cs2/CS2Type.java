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
package com.rs.cache.loaders.cs2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CS2Type {

    public static Map<Integer, CS2Type> attrTypes = new HashMap<>();

    private int intStackSize;
    private int stringStackSize;
    private int longStackSize;
    private String name;
    public char charDesc;
    private boolean structure;

    //BASE TYPES
    public static CS2Type VOID = new CS2Type(0, 0, 0, "void", '\0');
    public static CS2Type BOOLEAN = new CS2Type(1, 0, 0, "boolean", '1');
    public static CS2Type INT = new CS2Type(1, 0, 0, "int", 'i');
    public static CS2Type FONTMETRICS = new CS2Type(1, 0, 0, "FontMetrics", 'f');
    public static CS2Type SPRITE = new CS2Type(1, 0, 0, "Sprite", 'd');
    public static CS2Type MODEL = new CS2Type(1, 0, 0, "Model", 'm');
    public static CS2Type MIDI = new CS2Type(1, 0, 0, "Midi", 'M');
    public static CS2Type ENUM = new CS2Type(1, 0, 0, "Enum", 'g');
    public static CS2Type STRUCT = new CS2Type(1, 0, 0, "Struct", 'J');
    public static CS2Type CHAR = new CS2Type(1, 0, 0, "char", 'z');
    public static CS2Type CONTAINER = new CS2Type(1, 0, 0, "Container", 'v');
    public static CS2Type STRING = new CS2Type(0, 1, 0, "string", 's');
    public static CS2Type LONG = new CS2Type(0, 0, 1, "long", (char) 0xA7);
    public static CS2Type ICOMPONENT = new CS2Type(1, 0, 0, "IComponent", 'I');
    public static CS2Type LOCATION = new CS2Type(1, 0, 0, "Location", 'c');
    public static CS2Type ITEM = new CS2Type(1, 0, 0, "Item", 'o');
    //    public static CS2Type ITEM_NAMED = new CS2Type(1, 0, 0, "Item", 'O', false);
    public static CS2Type COLOR = new CS2Type(1, 0, 0, "Color", 'i'); //Not a real type, but helps us know where we need to convert int to hex notation
    public static CS2Type IDENTIKIT = new CS2Type(1, 0, 0, "Identikit", 'K');
    public static CS2Type ANIM = new CS2Type(1, 0, 0, "Animation", 'A');
    public static CS2Type MAPID = new CS2Type(1, 0, 0, "Map", '`');
    public static CS2Type GRAPHIC = new CS2Type(1, 0, 0, "SpotAnim", 't');
    public static CS2Type SKILL = new CS2Type(1, 0, 0, "Skill", 'S');
    public static CS2Type NPCDEF = new CS2Type(1, 0, 0, "NpcDef", 'n');
    public static CS2Type QCPHRASE = new CS2Type(1, 0, 0, "QcPhrase", 'e');
    public static CS2Type CHATCAT = new CS2Type(1, 0, 0, "QcCat", 'k');
    public static CS2Type TEXTURE = new CS2Type(1, 0, 0, "Texture", 'x');
    public static CS2Type STANCE = new CS2Type(1, 0, 0, "Stance", (char) 0x20AC);
    public static CS2Type SPELL = new CS2Type(1, 0, 0, "Spell", '@'); //target cursor?
    public static CS2Type CATEGORY = new CS2Type(1, 0, 0, "Category", 'y');
    public static CS2Type SOUNDEFFECT = new CS2Type(1, 0, 0, "SoundEff", (char) 0xAB);


    //    public static CS2Type VARINT = new CS2Type(0, 0, 0, "int...", 'Y'); //'Trigger/varargs'
    public static CS2Type CALLBACK = new CS2Type(0, 0, 0, "Callback", '\0'); //not real type


    public static CS2Type UNKNOWN = new CS2Type(0, 0, 0, "??", '\0');

    public static CS2Type[] TYPE_LIST = new CS2Type[]{VOID, CALLBACK, BOOLEAN, INT, FONTMETRICS, SPRITE, MODEL, MIDI, LOCATION, CHAR, STRING, LONG, UNKNOWN, ICOMPONENT, ITEM, COLOR, CONTAINER, ENUM, STRUCT, IDENTIKIT, ANIM, MAPID, GRAPHIC, SKILL, NPCDEF, QCPHRASE, CHATCAT, TEXTURE, STANCE, SPELL, CATEGORY, SOUNDEFFECT};
    private static List<CS2Type> cache = new ArrayList<CS2Type>();

    //TODO: Refactor this
    public CS2Type(int iss, int sss, int lss, String name, char c) {
        this.intStackSize = iss;
        this.stringStackSize = sss;
        this.longStackSize = lss;
        this.name = name;
        this.charDesc = c;
        this.structure = false;
        composite.add(this);
    }

    public List<CS2Type> composite = new ArrayList<>();

    private CS2Type(List<CS2Type> struct) {
        for (CS2Type t : struct) {
            this.intStackSize += t.intStackSize;
            this.stringStackSize += t.stringStackSize;
            this.longStackSize += t.longStackSize;
            composite.addAll(t.composite);
        }
        structure = true;
        name = "";
        cache.add(this);
    }

    public static CS2Type of(List<CS2Type> typeList) {
        if (typeList.size() == 1) {
            return typeList.get(0);
        }
        find:
        for (CS2Type other : cache) {
            if (other.composite.size() != typeList.size()) {
                continue;
            }
            for (int i = 0; i < other.composite.size(); i++) {
                if (other.composite.get(i) != typeList.get(i)) {
                    continue find;
                }
            }
            return other;
        }
        return new CS2Type(typeList);
    }

    public boolean isStructure() {
        return structure;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int stackHash = structure ? (1 << 9) : 0;
        stackHash |= intStackSize & 0x7;
        stackHash |= (stringStackSize & 0x7) << 3;
        stackHash |= (longStackSize & 0x7) << 6;
        int nameHash = (name.length() & 0x7) | (name.length() << 3);
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c & 0x1) != 0)
                nameHash += nameHash + (nameHash / c);
            else
                nameHash += nameHash - (nameHash * c);
        }
        return stackHash | (nameHash << 11);
    }

    public boolean equals(CS2Type other) {
        if (this.structure != other.structure) {
            return false;
        }
        if (this.composite.size() != other.composite.size()) {
            return false;
        }
        for (int i = 0; i < composite.size(); i++) {
            if (composite.get(i) != other.composite.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCompatible(CS2Type other) {
        //TODO: Order should be same too, but only used for simple types?
        return other == CS2Type.UNKNOWN || (intStackSize == other.intStackSize && stringStackSize == other.stringStackSize && longStackSize == other.longStackSize);
    }

    public static CS2Type forDesc(String desc) {
        for (int i = 0; i < TYPE_LIST.length; i++)
            if (desc.equals(TYPE_LIST[i].toString()))
                return TYPE_LIST[i];
        if (!desc.contains("{"))
            return null;
        String[] spl = desc.split("\\{");
        @SuppressWarnings("unused")
		String name = spl[0];
        String stackDesc = spl[1].substring(0, spl[1].length() - 1);
        String[] stackSpl = stackDesc.split("\\,");

        List<CS2Type> composite = new LinkedList<>();
        for (String s : stackSpl) {
            composite.add(forDesc(s.trim()));
        }

        return CS2Type.of(composite);
    }

    public static CS2Type forJagexDesc(char desc) {
        switch (desc) {
            case '\0':
                return VOID;
            case 0xA7:
                return LONG;
            case 'i':
                return INT;
            case 'z':
                return CHAR;
            case 's':
                return STRING;
            case '1':
                return BOOLEAN;
            case 'o':
            case 'O':
                //One of these is actually NAMED_ITEM?
                return ITEM;
            case 'A':
                return ANIM;
            case 'S':
                return SKILL;
            case 't':
                return GRAPHIC;
            case 'c':
                return LOCATION;
            case 'n':
                return NPCDEF;
            case 'J':
                return STRUCT;
            case 'g':
                return ENUM;
            case 'f':
                return FONTMETRICS;
            case 'd':
                return SPRITE;
            case 'm':
                return MODEL;
            case 'M':
                return MIDI;
            case 'K':
                return IDENTIKIT;
            case 'v':
                return CONTAINER;
            case 'I':
                return ICOMPONENT;
            case 'e':
                return QCPHRASE;
            case 'k':
                return CHATCAT;
            case 0x20AC:
                return STANCE;
            case 'x':
                return TEXTURE;
            case '@':
                return SPELL;
            case '`':
                return MAPID;
            case 'y':
                return CATEGORY;
            case 0xAB:
                return SOUNDEFFECT;
//            case 'P':
//                return SYNTH;
//'l' LOC (object)
            //More int based types...
            default:
                return INT;
        }

    }


    @Override
    public String toString() {
        if (structure) {
            StringBuilder s = new StringBuilder();
            boolean first = true;
            for (CS2Type t : composite) {
                if (!first) {
                    s.append(", ");
                }
                first = false;
                s.append(t);
            }
            return s.toString();
        } else {
            return this.name;
        }
    }
}
