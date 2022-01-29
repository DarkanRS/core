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
package com.rs.lib;

import java.math.BigInteger;

public class Constants {
	
	/**
	 * Tokens and keys
	 */
	public static String CAPTCHA_SITE = "6Lfob18UAAAAAEx08Pvihg-vpSZ_wsFtG1aPotQw";
	public static String CAPTCHA_SECRET = "6Lfob18UAAAAAC1as5gXon-vEZYBdcLg7nt4pG3S";
	public static final String GRAB_SERVER_TOKEN = "ev9+VAp5/tMKeNR/7MOuH6lKWS+rGkHK";
	public static final String WORLD_TOKEN = "ev9+VAp5/tMKeNR/7MOuH6lKWS+rGkHK";
	public static final int[] GRAB_SERVER_KEYS = { 1441, 78700, 44880, 39771, 363186, 44375, 0, 16140, 7316, 271148, 810710, 216189, 379672, 454149, 933950, 21006, 25367, 17247, 1244, 1, 14856, 1494, 119, 882901, 1818764, 3963, 3618 };
	public static final BigInteger RSA_PRIVATE_MODULUS = new BigInteger("117525752735533423040644219776209926525585489242340044375332234679786347045466594509203355398209678968096551043842518449703703964361320462967286756268851663407950384008240524570966471744081769815157355561961607944067477858512067883877129283799853947605780903005188603658779539811385137666347647991072028080201");
	public static final BigInteger RSA_PRIVATE_EXPONENT = new BigInteger("45769714620275867926001532284788836149236590657678028481492967724067121406860916606777808563536714166085238449913676219414798301454048585933351540049893959827785868628572203706265915752274580525376826724019249600701154664022299724373133271944352291456503171589594996734220177420375212353960806722706846977073");
	public static final String SERVER_NAME = "Darkan";
	
	/**
	 * Version/networking related static variables
	 */
	public static int CLIENT_BUILD = 727;
	public static int CUSTOM_CLIENT_BUILD = 1;
	public static int CLIENT_VERSION = 6;
	
	public static int PACKET_SIZE_LIMIT = 7500;
	public static final long WORLD_CYCLE_NS = 600000000L;
	public static final long WORLD_CYCLE_MS = WORLD_CYCLE_NS / 1000000L;
	
	/*
	 * RS Related constants
	 */
	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Constitution", "Ranged", "Prayer", "Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblore", "Agility",
	"Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction", "Summoning", "Dungeoneering" };
	
	public static final int 
	ATTACK = 0, 
	DEFENSE = 1, 
	STRENGTH = 2, 
	HITPOINTS = 3, 
	RANGE = 4, 
	PRAYER = 5, 
	MAGIC = 6, 
	COOKING = 7, 
	WOODCUTTING = 8, 
	FLETCHING = 9, 
	FISHING = 10, 
	FIREMAKING = 11, 
	CRAFTING = 12, 
	SMITHING = 13, 
	MINING = 14, 
	HERBLORE = 15,
	AGILITY = 16, 
	THIEVING = 17, 
	SLAYER = 18, 
	FARMING = 19, 
	RUNECRAFTING = 20, 
	HUNTER = 21, 
	CONSTRUCTION = 22, 
	SUMMONING = 23, 
	DUNGEONEERING = 24;
}
