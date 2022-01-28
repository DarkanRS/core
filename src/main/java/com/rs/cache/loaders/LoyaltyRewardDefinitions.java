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
package com.rs.cache.loaders;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.rs.lib.game.Item;

public class LoyaltyRewardDefinitions {
	
	private static int NAME = 1930;
	private static int ITEM_ID = 1935;
	private static int ENUM = 1995;
	
	public enum Type {
		AURA, EFFECT, EMOTE, COSTUME, TITLE, RECOLOR
	}
	
	public enum Tab {
		AURAS(5182, 2229, 66, 1, Reward.ODDBALL, Reward.POISON_PURGE, Reward.FRIEND_IN_NEED, Reward.KNOCK_OUT, Reward.SHARPSHOOTER, Reward.RUNIC_ACCURACY, Reward.SUREFOOTED, Reward.REVERENCE, Reward.CALL_OF_THE_SEA, Reward.JACK_OF_TRADES, Reward.GREATER_POISON_PURGE, Reward.GREATER_RUNIC_ACCURACY, Reward.GREATER_SHARPSHOOTER, Reward.GREATER_CALL_OF_THE_SEA, Reward.GREATER_REVERENCE, Reward.GREATER_SUREFOOTED, Reward.LUMBERJACK, Reward.GREATER_LUMBERJACK, Reward.QUARRYMASTER, Reward.GREATER_QUARRYMASTER, Reward.FIVE_FINGER_DISCOUNT, Reward.GREATER_FIVE_FINGER_DISCOUNT, Reward.RESOURCEFUL, Reward.EQUILIBRIUM, Reward.INSPIRATION, Reward.VAMPYRISM, Reward.PENANCE, Reward.WISDOM, Reward.AEGIS, Reward.REGENERATION, Reward.DARK_MAGIC, Reward.BERSERKER, Reward.ANCESTOR_SPIRITS, Reward.GREENFINGERS, Reward.GREATER_GREENFINGERS, Reward.MASTER_GREENFINGERS, Reward.TRACKER, Reward.GREATER_TRACKER, Reward.MASTER_TRACKER, Reward.SALVATION, Reward.GREATER_SALVATION, Reward.MASTER_SALVATION, Reward.CORRUPTION, Reward.GREATER_CORRUPTION, Reward.MASTER_CORRUPTION, Reward.MASTER_FIVE_FINGER_DISCOUNT, Reward.MASTER_QUARRYMASTER, Reward.MASTER_LUMBERJACK, Reward.MASTER_POISON_PURGE, Reward.MASTER_RUNIC_ACCURACY, Reward.MASTER_SHARPSHOOTER, Reward.MASTER_CALL_OF_THE_SEA, Reward.MASTER_REVERENCE, Reward.MASTER_KNOCK_OUT, Reward.SUPREME_SALVATION, Reward.SUPREME_CORRUPTION, Reward.HARMONY, Reward.GREATER_HARMONY, Reward.MASTER_HARMONY, Reward.SUPREME_HARMONY, Reward.INVIGORATE, Reward.GREATER_INVIGORATE, Reward.MASTER_INVIGORATE, Reward.SUPREME_INVIGORATE, Reward.SUPREME_FIVE_FINGER_DISCOUNT, Reward.SUPREME_QUARRYMASTER, Reward.SUPREME_LUMBERJACK, Reward.SUPREME_POISON_PURGE, Reward.SUPREME_RUNIC_ACCURACY, Reward.SUPREME_SHARPSHOOTER, Reward.SUPREME_CALL_OF_THE_SEA, Reward.SUPREME_REVERENCE, Reward.SUPREME_TRACKER, Reward.SUPREME_GREENFINGERS),
		EFFECTS(5724, 2540, 67, 9, Reward.INFERNAL_GAZE, Reward.SERENE_GAZE, Reward.VERNAL_GAZE, Reward.NOCTURNAL_GAZE, Reward.MYSTICAL_GAZE, Reward.BLAZING_GAZE, Reward.ABYSSAL_GAZE, Reward.DIVINE_GAZE),
		EMOTES(3875, 2230, 68, 2, Reward.CAT_FIGHT, Reward.TALK_TO_THE_HAND, Reward.SHAKE_HANDS, Reward.HIGH_FIVE, Reward.FACE_PALM, Reward.SURRENDER, Reward.LEVITATE, Reward.MUSCLE_MAN_POSE, Reward.ROFL, Reward.BREATHE_FIRE, Reward.STORM, Reward.SNOW, Reward.HEAD_IN_THE_SAND, Reward.HULA_HOOP, Reward.DISAPPEAR, Reward.GHOST, Reward.BRING_IT, Reward.PALM_FIST, Reward.EVIL_LAUGH, Reward.GOLF_CLAP, Reward.LOLCANO, Reward.INFERNAL_POWER, Reward.DIVINE_POWER, Reward.YOURE_DEAD, Reward.SCREAM, Reward.TORNADO, Reward.ROFLCOPTER, Reward.NATURES_MIGHT, Reward.INNER_POWER, Reward.WEREWOLF_TRANSFORMATION),
		COSTUMES(5189, 2231, 69, 3, Reward.DERVISH_OUTFIT, Reward.EASTERN_OUTFIT, Reward.SAXON_OUTFIT, Reward.SAMBA_OUTFIT, Reward.THEATRICAL_OUTFIT, Reward.PHARAOH_OUTFIT, Reward.CHANGSHAN_OUTFIT, Reward.SILKEN_OUTFIT, Reward.COLONISTS_OUTFIT, Reward.AZTEC_OUTFIT, Reward.HIGHLANDER_OUTFIT, Reward.MUSKETEER_OUTFIT, Reward.ELVEN_OUTFIT, Reward.WEREWOLF_COSTUME),
		TITLES(5184, 2232, 70, 4, Reward.SIR, Reward.LORD, Reward.DUDERINO, Reward.LIONHEART, Reward.HELLRAISER, Reward.CRUSADER, Reward.DESPERADO, Reward.BARON, Reward.COUNT, Reward.OVERLORD, Reward.BANDITO, Reward.DUKE, Reward.KING, Reward.BIG_CHEESE, Reward.BIGWIG, Reward.WUNDERKIND, Reward.EMPEROR, Reward.PRINCE, Reward.WITCH_KING, Reward.ARCHON, Reward.JUSTICIAR, Reward.THE_AWESOME, Reward.THE_MAGNIFICENT, Reward.THE_UNDEFEATED, Reward.THE_STRANGE, Reward.THE_DIVINE, Reward.THE_FALLEN, Reward.THE_WARRIOR, Reward.ATHLETE),
		RECOLOR(5183, 2232, 71, 5, Reward.PET_ROCK, Reward.ROBIN_HOOD_HAT, Reward.STAFF_OF_LIGHT, Reward.GNOME_SCARF, Reward.LUNAR_EQUIPMENT, Reward.FULL_SLAYER_HELMET, Reward.RANGER_BOOTS, Reward.RING_OF_STONE, Reward.ANCIENT_STAFF, Reward.MAGES_BOOK, Reward.TOP_HAT);		
		
		private int csMapId;
		public int unlockConfig;
		private int favoriteComponent;
		private int buyComponent;
		public int configId;
		private Reward[] rewards;
		
		private static HashMap<Integer, Tab> map = new HashMap<>();
		static {
			for (Tab t : Tab.values()) {
				map.put(t.buyComponent, t);
				map.put(t.favoriteComponent, t);
			}
		}
		
		public static Tab forId(int componentId) {
			return map.get(componentId);
		}
		
		private Tab(int reqScript, int unlockConfig, int buyComponent, int configId, Reward... rewards) {
			this.csMapId = reqScript;
			this.unlockConfig = unlockConfig;
			this.buyComponent = buyComponent;
			this.favoriteComponent = buyComponent-6;
			this.configId = configId;
			this.rewards = rewards;
		}
		
		public int getBuyComponent() {
			return buyComponent;
		}
		
		public int getFavoriteComponent() {
			return favoriteComponent;
		}
		
		public boolean isBuyComponent(int component) {
			return buyComponent == component;
		}
		
		public boolean isFavoriteComponent(int component) {
			return favoriteComponent == component;
		}
		
		public Reward getReward(int slot) {
			if (slot >= rewards.length)
				return null;
			return rewards[slot];
		}
		
		public int getCSMapId(boolean male) {
			if (this == Tab.COSTUMES) {
				return male ? 5189 : 5188;
			}
			return csMapId;
		}

		public Reward[] getRewards() {
			return rewards;
		}
	}
	
	public enum Reward {
		/*
		 * AURAS
		 */
		ODDBALL(Type.AURA, 0x0, -1, 20957, 2000),
		POISON_PURGE(Type.AURA, 0x1, -1, 20958, 2750),
		FRIEND_IN_NEED(Type.AURA, 0x2, -1, 20963, 3500),
		KNOCK_OUT(Type.AURA, 0x3, -1, 20961, 3500),
		SHARPSHOOTER(Type.AURA, 0x4, -1, 20967, 4250),
		RUNIC_ACCURACY(Type.AURA, 0x5, -1, 20962, 4250),
		SUREFOOTED(Type.AURA, 0x6, -1, 20964, 5000),
		REVERENCE(Type.AURA, 0x7, -1, 20965, 5000),
		CALL_OF_THE_SEA(Type.AURA, 0x8, -1, 20966, 5000),
		JACK_OF_TRADES(Type.AURA, 0x9, -1, 20959, 15000),
		GREATER_POISON_PURGE(Type.AURA, 0xA, 20958, 22268, 12250),
		GREATER_RUNIC_ACCURACY(Type.AURA, 0xB, 20962, 22270, 16750),
		GREATER_SHARPSHOOTER(Type.AURA, 0xC, 20967, 22272, 16750),
		GREATER_CALL_OF_THE_SEA(Type.AURA, 0xD, 20966, 22274, 14000),
		GREATER_REVERENCE(Type.AURA, 0xE, 20965, 22276, 16000),
		GREATER_SUREFOOTED(Type.AURA, 0xF, 20964, 22278, 12000),
		LUMBERJACK(Type.AURA, 0x10, -1, 22280, 5000),
		GREATER_LUMBERJACK(Type.AURA, 0x11, 22280, 22282, 14000),
		QUARRYMASTER(Type.AURA, 0x12, -1, 22284, 5000),
		GREATER_QUARRYMASTER(Type.AURA, 0x13, 22284, 22286, 14000),
		FIVE_FINGER_DISCOUNT(Type.AURA, 0x14, -1, 22288, 5000),
		GREATER_FIVE_FINGER_DISCOUNT(Type.AURA, 0x15, 22288, 22290, 14000),
		RESOURCEFUL(Type.AURA, 0x16, -1, 22292, 23000),
		EQUILIBRIUM(Type.AURA, 0x17, -1, 22294, 23000),
		INSPIRATION(Type.AURA, 0x18, -1, 22296, 23000),
		VAMPYRISM(Type.AURA, 0x19, -1, 22298, 23000),
		PENANCE(Type.AURA, 0x1A, -1, 22300, 23000),
		WISDOM(Type.AURA, 0x1B, -1, 22302, 40000),
		AEGIS(Type.AURA, 0x1C, -1, 22889, 84000),
		REGENERATION(Type.AURA, 0x1D, -1, 22893, 45000),
		DARK_MAGIC(Type.AURA, 0x1E, -1, 22891, 42500),
		BERSERKER(Type.AURA, 0x1F, -1, 22897, 50000),
		ANCESTOR_SPIRITS(Type.AURA, 0x20, -1, 22895, 50000),
		GREENFINGERS(Type.AURA, 0x21, -1, 22883, 5000),
		GREATER_GREENFINGERS(Type.AURA, 0x22, 22883, 22885, 16000),
		MASTER_GREENFINGERS(Type.AURA, 0x23, 22885, 22887, 29000),
		TRACKER(Type.AURA, 0x24, -1, 22927, 5000),
		GREATER_TRACKER(Type.AURA, 0x25, 22927, 22929, 16000),
		MASTER_TRACKER(Type.AURA, 0x26, 22929, 22931, 29000),
		SALVATION(Type.AURA, 0x27, -1, 22899, 5000),
		GREATER_SALVATION(Type.AURA, 0x28, 22899, 22901, 12000),
		MASTER_SALVATION(Type.AURA, 0x29, 22901, 22903, 30500),
		CORRUPTION(Type.AURA, 0x2A, -1, 22905, 5000),
		GREATER_CORRUPTION(Type.AURA, 0x2B, 22905, 22907, 12000),
		MASTER_CORRUPTION(Type.AURA, 0x2C, 22907, 22909, 30500),
		MASTER_FIVE_FINGER_DISCOUNT(Type.AURA, 0x2D, 22290, 22911, 33500),
		MASTER_QUARRYMASTER(Type.AURA, 0x2E, 22286, 22913, 33500),
		MASTER_LUMBERJACK(Type.AURA, 0x2F, 22282, 22915, 33500),
		MASTER_POISON_PURGE(Type.AURA, 0x30, 22268, 22917, 32500),
		MASTER_RUNIC_ACCURACY(Type.AURA, 0x31, 22270, 22919, 29000),
		MASTER_SHARPSHOOTER(Type.AURA, 0x32, 22272, 22921, 29000),
		MASTER_CALL_OF_THE_SEA(Type.AURA, 0x33, 22274, 22923, 33500),
		MASTER_REVERENCE(Type.AURA, 0x34, 22276, 22925, 36500),
		MASTER_KNOCK_OUT(Type.AURA, 0x35, 20961, 22933, 50000),
		SUPREME_SALVATION(Type.AURA, 0x36, 22903, 23876, 63500),
		SUPREME_CORRUPTION(Type.AURA, 0x37, 22909, 23874, 63500),
		HARMONY(Type.AURA, 0x38, -1, 23848, 5000),
		GREATER_HARMONY(Type.AURA, 0x39, 23848, 23850, 12000),
		MASTER_HARMONY(Type.AURA, 0x3A, 23850, 23852, 30500),
		SUPREME_HARMONY(Type.AURA, 0x3B, 23852, 23854, 63500),
		INVIGORATE(Type.AURA, 0x3C, -1, 23840, 5000),
		GREATER_INVIGORATE(Type.AURA, 0x3D, 23840, 23842, 16000),
		MASTER_INVIGORATE(Type.AURA, 0x3E, 23842, 23844, 36500),
		SUPREME_INVIGORATE(Type.AURA, 0x3F, 23844, 23846, 57500),
		SUPREME_FIVE_FINGER_DISCOUNT(Type.AURA, 0x40, 22911, 23856, 58500),
		SUPREME_QUARRYMASTER(Type.AURA, 0x41, 22913, 23858, 58500),
		SUPREME_LUMBERJACK(Type.AURA, 0x42, 22915, 23860, 58500),
		SUPREME_POISON_PURGE(Type.AURA, 0x43, 22917, 23862, 51500),
		SUPREME_RUNIC_ACCURACY(Type.AURA, 0x44, 22919, 23864, 57000),
		SUPREME_SHARPSHOOTER(Type.AURA, 0x45, 22921, 23866, 57000),
		SUPREME_CALL_OF_THE_SEA(Type.AURA, 0x46, 22923, 23868, 58500),
		SUPREME_REVERENCE(Type.AURA, 0x47, 22925, 23870, 57500),
		SUPREME_TRACKER(Type.AURA, 0x48, 22931, 23872, 61000),
		SUPREME_GREENFINGERS(Type.AURA, 0x49, 22887, 23878, 61000),
		
		/*
		 * Gazes
		 */
		INFERNAL_GAZE(Type.EFFECT, 0x0, -1, 23880, 18000),
		SERENE_GAZE(Type.EFFECT, 0x1, -1, 23882, 18000),
		VERNAL_GAZE(Type.EFFECT, 0x2, -1, 23884, 18000),
		NOCTURNAL_GAZE(Type.EFFECT, 0x3, -1, 23886, 18000),
		MYSTICAL_GAZE(Type.EFFECT, 0x4, -1, 23888, 18000),
		BLAZING_GAZE(Type.EFFECT, 0x5, -1, 23890, 18000),
		ABYSSAL_GAZE(Type.EFFECT, 0x6, -1, 23892, 18000),
		DIVINE_GAZE(Type.EFFECT, 0x7, -1, 23894, 18000),
		
		/*
		 * EMOTES
		 */
		CAT_FIGHT(Type.EMOTE, 0x0, -1, 21314, 500),
		TALK_TO_THE_HAND(Type.EMOTE, 0x1, -1, 21306, 750),
		SHAKE_HANDS(Type.EMOTE, 0x2, -1, 21300, 750),
		HIGH_FIVE(Type.EMOTE, 0x3, -1, 21302, 750),
		FACE_PALM(Type.EMOTE, 0x4, -1, 21312, 750),
		SURRENDER(Type.EMOTE, 0x5, -1, 21320, 750),
		LEVITATE(Type.EMOTE, 0x6, -1, 21304, 1000),
		MUSCLE_MAN_POSE(Type.EMOTE, 0x7, -1, 21298, 1000),
		ROFL(Type.EMOTE, 0x8, -1, 21316, 1000),
		BREATHE_FIRE(Type.EMOTE, 0x9, -1, 21318, 1000),
		STORM(Type.EMOTE, 0xA, -1, 21308, 4000),
		SNOW(Type.EMOTE, 0xB, -1, 21310, 4000),
		HEAD_IN_THE_SAND(Type.EMOTE, 0xC, -1, 21874, 6000),
		HULA_HOOP(Type.EMOTE, 0xD, -1, 21876, 4000),
		DISAPPEAR(Type.EMOTE, 0xE, -1, 21878, 12000),
		GHOST(Type.EMOTE, 0xF, -1, 21880, 12000),
		BRING_IT(Type.EMOTE, 0x10, -1, 21882, 1000),
		PALM_FIST(Type.EMOTE, 0x11, -1, 21884, 750),
		EVIL_LAUGH(Type.EMOTE, 0x12, -1, 22512, 1000),
		GOLF_CLAP(Type.EMOTE, 0x13, -1, 22514, 750),
		LOLCANO(Type.EMOTE, 0x14, -1, 22516, 6000),
		INFERNAL_POWER(Type.EMOTE, 0x15, -1, 22518, 12000),
		DIVINE_POWER(Type.EMOTE, 0x16, -1, 22520, 12000),
		YOURE_DEAD(Type.EMOTE, 0x17, -1, 22522, 1000),
		SCREAM(Type.EMOTE, 0x18, -1, 22524, 6000),
		TORNADO(Type.EMOTE, 0x19, -1, 22526, 12000),
		ROFLCOPTER(Type.EMOTE, 0x1A, -1, 23832, 6000),
		NATURES_MIGHT(Type.EMOTE, 0x1B, -1, 23834, 12000),
		INNER_POWER(Type.EMOTE, 0x1C, -1, 23836, 6000),
		WEREWOLF_TRANSFORMATION(Type.EMOTE, 0x1D, -1, 23838, 12000),
		
		/*
		 * COSTUMES
		 */
		DERVISH_OUTFIT(Type.COSTUME, 0x0, -1, new Item[][] {
			//male
			new Item[] { new Item(20970, 2), new Item(20980, 2), new Item(20990, 2), new Item(21000, 2) },
			//female
			new Item[] { new Item(21010, 2), new Item(21020, 2), new Item(21030, 2), new Item(21040, 2) },
		}, 1000),
		EASTERN_OUTFIT(Type.COSTUME, 0x1, -1, new Item[][] {
			//male
			new Item[] { new Item(21050), new Item(21052, 2), new Item(21062, 2), new Item(21072) },
			//female
			new Item[] { new Item(21074), new Item(21076, 2), new Item(21086, 2), new Item(21096, 2) },
		}, 1000),
		SAXON_OUTFIT(Type.COSTUME, 0x2, -1, new Item[][] {
			//male
			new Item[] { new Item(21106, 2), new Item(21116, 2), new Item(21126, 2), new Item(21136, 2) },
			//female
			new Item[] { new Item(21146, 2), new Item(21156, 2), new Item(21166, 2), new Item(21176) },
		}, 1000),
		SAMBA_OUTFIT(Type.COSTUME, 0x3, -1, new Item[][] {
			//male
			new Item[] { new Item(21178, 2), new Item(21188, 2), new Item(21198, 2), new Item(21208, 2) },
			//female
			new Item[] { new Item(21218, 2), new Item(21228, 2), new Item(21238, 2), new Item(21248) },
		}, 1000),
		THEATRICAL_OUTFIT(Type.COSTUME, 0x4, -1, new Item[][] {
			//male
			new Item[] { new Item(21887, 2), new Item(21897, 2), new Item(21907, 2), new Item(21917, 2) },
			//female
			new Item[] { new Item(21927, 2), new Item(21937, 2), new Item(21947, 2), new Item(21957, 2) },
		}, 2000),
		PHARAOH_OUTFIT(Type.COSTUME, 0x5, -1, new Item[][] {
			//male
			new Item[] { new Item(21967, 2), new Item(21977, 2), new Item(21987, 2), new Item(21997, 2) },
			//female
			new Item[] { new Item(22007, 2), new Item(22017, 2), new Item(22027, 2), new Item(22037, 2) },
		}, 2000),
		CHANGSHAN_OUTFIT(Type.COSTUME, 0x6, -1, new Item[][] {
			//male
			new Item[] { new Item(22047, 2), new Item(22057, 2), new Item(22067, 2), new Item(22077, 2) },
			//female
			new Item[] { new Item(22087, 2), new Item(22097, 2), new Item(22107, 2), new Item(22117, 2) },
		}, 2000),
		SILKEN_OUTFIT(Type.COSTUME, 0x7, -1, new Item[][] {
			//male
			new Item[] { new Item(22127, 2), new Item(22137, 2), new Item(22147, 2), new Item(22157, 2) },
			//female
			new Item[] { new Item(22167, 2), new Item(22177, 2), new Item(22187, 2), new Item(22197, 2) },
		}, 2000),
		COLONISTS_OUTFIT(Type.COSTUME, 0x8, -1, new Item[][] {
			//male
			new Item[] { new Item(22568, 2), new Item(22578, 2), new Item(22588, 2), new Item(22598, 2) },
			//female
			new Item[] { new Item(22608, 2), new Item(22618, 2), new Item(22628, 2), new Item(22638) }, //TODO cancer shoes..
		}, 4000),
		AZTEC_OUTFIT(Type.COSTUME, 0x9, -1, new Item[][] {
			//male
			new Item[] { new Item(22723, 2), new Item(22733, 2), new Item(22743, 2), new Item(22753, 2) },
			//female
			new Item[] { new Item(22763, 2), new Item(22773, 2), new Item(22783, 2), new Item(22793, 2) },
		}, 4000),
		HIGHLANDER_OUTFIT(Type.COSTUME, 0xA, -1, new Item[][] {
			//male
			new Item[] { new Item(22643, 2), new Item(22653, 2), new Item(22663, 2), new Item(22673, 2) },
			//female
			new Item[] { new Item(22683, 2), new Item(22693, 2), new Item(22703, 2), new Item(22713, 2) },
		}, 4000),
		MUSKETEER_OUTFIT(Type.COSTUME, 0xB, -1, new Item[][] {
			//male
			new Item[] { new Item(22803, 2), new Item(22813, 2), new Item(22823, 2), new Item(22833, 2) },
			//female
			new Item[] { new Item(22843, 2), new Item(22853, 2), new Item(22863, 2), new Item(22873, 2) },
		}, 4000),
		ELVEN_OUTFIT(Type.COSTUME, 0xC, -1, new Item[][] {
			//male
			new Item[] { new Item(23912, 2), new Item(23922, 2), new Item(23932, 2), new Item(23942, 2) },
			//female
			new Item[] { new Item(23952, 2), new Item(23962, 2), new Item(23972, 2), new Item(23982, 2) },
		}, 8000),
		WEREWOLF_COSTUME(Type.COSTUME, 0xD, -1, new Item[][] {
			//male
			new Item[] { new Item(23992, 2), new Item(24002, 2), new Item(24012, 2), new Item(24022, 2), new Item(24032, 2) },
			//female
			new Item[] { new Item(24042, 2), new Item(24052, 2), new Item(24062, 2), new Item(24072, 2), new Item(24082, 2) },
		}, 8000),
		
		/*
		 * TITLES
		 */
		SIR(Type.TITLE, 0x0, -1, 5, 1000),
		LORD(Type.TITLE, 0x1, -1, 6, 1000),
		DUDERINO(Type.TITLE, 0x2, -1, 7, 4000),
		LIONHEART(Type.TITLE, 0x3, -1, 8, 4000),
		HELLRAISER(Type.TITLE, 0x4, -1, 9, 8000),
		CRUSADER(Type.TITLE, 0x5, -1, 10, 8000),
		DESPERADO(Type.TITLE, 0x6, -1, 11, 10000),
		BARON(Type.TITLE, 0x7, -1, 12, 10000),
		COUNT(Type.TITLE, 0x8, -1, 13, 15000),
		OVERLORD(Type.TITLE, 0x9, -1, 14, 15000),
		BANDITO(Type.TITLE, 0xA, -1, 15, 20000),
		DUKE(Type.TITLE, 0xB, -1, 16, 20000),
		KING(Type.TITLE, 0xC, -1, 17, 25000),
		BIG_CHEESE(Type.TITLE, 0xD, -1, 18, 25000),
		BIGWIG(Type.TITLE, 0xE, -1, 19, 25000),
		WUNDERKIND(Type.TITLE, 0xF, -1, 20, 50000),
		EMPEROR(Type.TITLE, 0x10, -1, 26, 30000),
		PRINCE(Type.TITLE, 0x11, -1, 27, 15000),
		WITCH_KING(Type.TITLE, 0x12, -1, 28, 50000),
		ARCHON(Type.TITLE, 0x13, -1, 29, 25000),
		JUSTICIAR(Type.TITLE, 0x14, -1, 30, 20000),
		THE_AWESOME(Type.TITLE, 0x15, -1, 31, 50000),
		THE_MAGNIFICENT(Type.TITLE, 0x16, -1, 32, 50000),
		THE_UNDEFEATED(Type.TITLE, 0x17, -1, 33, 50000),
		THE_STRANGE(Type.TITLE, 0x18, -1, 34, 50000),
		THE_DIVINE(Type.TITLE, 0x19, -1, 35, 50000),
		THE_FALLEN(Type.TITLE, 0x1A, -1, 36, 50000),
		THE_WARRIOR(Type.TITLE, 0x1B, -1, 37, 50000),
		ATHLETE(Type.TITLE, 0x1C, -1, 71, 5000),
		
		/*
		 * RECOLORS
		 */
		PET_ROCK(Type.RECOLOR, 0x10, -1, 3695, 1000),
		ROBIN_HOOD_HAT(Type.RECOLOR, 0x11, -1, 2581, 1000),
		STAFF_OF_LIGHT(Type.RECOLOR, 0x12, -1, 15486, 2000),
		GNOME_SCARF(Type.RECOLOR, 0x13, -1, 9470, 2000),
		LUNAR_EQUIPMENT(Type.RECOLOR, 0x14, -1, 9096, 2000),
		FULL_SLAYER_HELMET(Type.RECOLOR, 0x15, -1, 15492, 4000),
		RANGER_BOOTS(Type.RECOLOR, 0x16, -1, 2577, 4000),
		RING_OF_STONE(Type.RECOLOR, 0x17, -1, 6583, 4000),
		ANCIENT_STAFF(Type.RECOLOR, 0x18, -1, 4675, 6000),
		MAGES_BOOK(Type.RECOLOR, 0x19, -1, 6889, 6000),
		TOP_HAT(Type.RECOLOR, 0x1A, -1, 13101, 6000);
		
		private Type type;
		private int bit;
		private int preReq;
		private Item[][] items;
		private int price;
		
		private static HashMap<Integer, Reward> ITEMID_MAP = new HashMap<Integer, Reward>();
		private static HashMap<Integer, Reward> PREREQ_MAP = new HashMap<Integer, Reward>();
		
		static {
			for (Reward r : Reward.values()) {
				ITEMID_MAP.put(r.items[0][0].getId(), r);
				PREREQ_MAP.put(r.preReq, r);
			}
		}
		
		public static Reward forId(int itemId) {
			return ITEMID_MAP.get(itemId);
		}
		
		public static Reward forPreReq(int itemId) {
			return PREREQ_MAP.get(itemId);
		}

		private Reward(Type type, int bit, int preReq, Item[][] items, int price) {
			this.type = type;
			this.bit = bit;
			this.preReq = preReq;
			this.items = items;
			this.price = price;
		}
		
		private Reward(Type type, int bit, int preReq, int itemId, int price) {
			this(type, bit, preReq, new Item[][] { { new Item(itemId) } }, price);
		}
		
		public Reward getLowestTier() {
			Reward reward = this;
			while(reward.getPreReq() != -1)
				reward = Reward.forId(reward.preReq);
			return reward;
		}
		
		public Type getType() {
			return type;
		}
		
		public int getBit() {
			return bit;
		}

		public int getPreReq() {
			return preReq;
		}
		
		public Item getItem() {
			return items[0][0];
		}
		
		public Item[] getItems() {
			return items[0];
		}

		public Item[] getItems(int type) {
			return items[type];
		}

		public int getPrice() {
			return price;
		}
	}
	
	public static void main(String[] args) throws IOException {
		//Cache.init();
		dump();
	}
	
	@SuppressWarnings("unused")
	private static void dump() {
		for (Tab t : Tab.values()) {
			if (t != Tab.COSTUMES)
				continue;
			int bit = 0;
			int index = 0;
			String nameArr = "";
			EnumDefinitions tab = EnumDefinitions.getEnum(t.csMapId);
			//System.out.println("Tab parsed: " + t.name());
			for (int i = 0;i < tab.getSize();i++) {
				StructDefinitions reward = StructDefinitions.getStruct(tab.getIntValue(i));
				if (reward != null) {
					int bitVal = (bit + (t == Tab.RECOLOR ? 16 : 0));
					String name = reward.getStringValue(NAME).trim().toUpperCase().replaceAll(" ", "_");
					int itemId = reward.getIntValue(ITEM_ID);
					if (reward.getStringValue(ENUM).contains("_female"))
						name += "_F";
					if (reward.getStringValue(ENUM).contains("_male"))
						name += "_M";
					int num = 1;
					if (reward.getIntValue(1950) != 0) {
						num = 5;
					} else if (reward.getIntValue(1949) != 0) {
						num = 4;
					} else if (reward.getIntValue(1948) != 0) {
						num = 3;
					} else if (reward.getIntValue(1947) != 0) {
						num = 2;
					} else if (reward.getIntValue(1946) != 0) {
						num = 1;
					}
					
					int preReq = -1;
					if (reward.getIntValue(1989) != 0 && reward.getIntValue(1989) < itemId && preReq < itemId) {
						preReq = reward.getIntValue(1989);
					}
					if (reward.getIntValue(1990) != 0 && reward.getIntValue(1990) < itemId && preReq < itemId) {
						preReq = reward.getIntValue(1990);
					}
					if (reward.getIntValue(1991) != 0 && reward.getIntValue(1991) < itemId && preReq < itemId) {
						preReq = reward.getIntValue(1991);
					}
					
					String[] colors = new String[num];
					for (int j = 0;j < num;j++) {
						if (reward.getIntValue(1946+j) != 0) {
							colors[j] = StructDefinitions.getStruct(reward.getIntValue(1946+j)).getStringValue(1994);
						}
					}
					
					nameArr += "Reward."+name + (i == tab.getSize()-1 ? "" : ", ");
					//System.out.println(name+"(0x"+Integer.toHexString(bitVal).toUpperCase()+", "+preReq+", "+itemId+", "+num+", "+reward.getIntValue(PRICE)+"),");
					System.out.println(reward.getValues());
					System.out.println(Arrays.toString(colors));
//					if (i == tab.getSize()-1) {
//						System.out.println(nameArr);
//					}
					
					index++;
					bit++;
				}
			}
		}
	}

}
