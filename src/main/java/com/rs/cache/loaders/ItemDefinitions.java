package com.rs.cache.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rs.cache.ArchiveType;
import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.lib.Constants;
import com.rs.lib.game.Item;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.Utils;

public final class ItemDefinitions {

	private static final HashMap<Integer, ItemDefinitions> ITEM_DEFINITIONS = new HashMap<>();
	private static final HashMap<Integer, Integer> EQUIP_IDS = new HashMap<Integer, Integer>();

	public int id;
	public boolean loaded;
	public int value = 1;
	public int modelId;
	public int modelZoom = 2000;
	public int modelRotationX = 0;
	public int modelRotationY = 0;
	public int modelRotationZ = 0;
	public int modelOffsetX = 0;
	public int modelOffsetY = 0;
	public int[] originalModelColors;
	public int[] modifiedModelColors;
	public byte[] spriteRecolorIndices;
	public int[] originalTextureIds;
	public int[] modifiedTextureIds;
	public String name = "null";
	public boolean membersOnly = false;
	public int wearPos = -1;
	public int wearPos2 = -1;
	public int wearPos3 = -1;
	public int maleEquip1 = -1;
	public int maleEquip2 = -1;
	public int maleEquip3 = -1;
	public int femaleEquip1 = -1;
	public int femaleEquip2 = -1;
	public int femaleEquip3 = -1;
	public int maleWearXOffset = 0;
	public int femaleWearXOffset = 0;
	public int maleWearYOffset = 0;
	public int femaleWearYOffset = 0;
	public int maleWearZOffset = 0;
	public int femaleWearZOffset = 0;
	public int maleHead1 = -1;
	public int maleHead2 = -1;
	public int femaleHead1 = -1;
	public int femaleHead2 = -1;
	public int teamId = 0;
	public String[] groundOptions;
	public int stackable = 0;
	public String[] inventoryOptions;
	public int multiStackSize = -1;
	public int tooltipColor;
	public boolean hasTooltipColor = false;
	private boolean grandExchange = false;
	public int unknownInt6 = 0;
	public int certId = -1;
	public int certTemplateId = -1;
	public int resizeX = 128;
	public int[] stackIds;
	public int[] stackAmounts;
	public int resizeY = 128;
	public int resizeZ = 128;
	public int ambient = 0;
	public int contrast = 0;
	public int lendId = -1;
	public int lendTemplateId = -1;
	public int unknownInt18 = -1;
	public int unknownInt19 = -1;
	public int unknownInt20 = -1;
	public int unknownInt21 = -1;
	public int customCursorOp1 = -1;
	public int customCursorId1 = -1;
	public int customCursorOp2 = -1;
	public int customCursorId2 = -1;
	public int[] quests;
	public int[] bonuses = new int[18];
	public int pickSizeShift = 0;
	public int bindId = -1;
	public int bindTemplateId = -1;
	

	// extra added
	public boolean noted;
	public boolean lended;

	private HashMap<Integer, Object> clientScriptData;
	private HashMap<Integer, Integer> itemRequiriments;

	/**
	 * 1 = thrown weapons? and cannonballs 2 = arrows 3 = bolts 4 = construction
	 * materials 5 = flatpacks 6 = cooking items 7 = cosmetic items 8 = crafting
	 * items 9 = summoning pouches 10 = junky farming produce 11 = fletching items
	 * 12 = eatable items 13 = herblore secondaries and unf pots 14 = hunter
	 * clothing/traps/bait 15 = hunter produce 16 = jewelry 17 = magic armour 18 =
	 * staves 19 = low tier melee armour 20 = mid tier melee armour 21 = high tier
	 * melee armour 22 = low level melee weapons 23 = mid tier melee weapons 24 =
	 * high tier melee weapons 25 = smithables 26 = finished potions 27 = prayer
	 * enhancing items 28 = prayer training items bones/ashes 29 = ranged armour 30
	 * = ranged weapons 31 = runecrafting training items 32 = teleport items 33 =
	 * seeds 34 = summoning scrolls 35 = crafting misc items/tools 36 = woodcutting
	 * produce
	 */
	public int getItemCategory() {
		if (clientScriptData == null)
			return -1;
		Object protectedOnDeath = clientScriptData.get(2195);
		if (protectedOnDeath != null && protectedOnDeath instanceof Integer)
			return (Integer) protectedOnDeath;
		return -1;
	}

	public int getIdk() {
		if (clientScriptData == null)
			return -1;
		Object protectedOnDeath = clientScriptData.get(1397);
		if (protectedOnDeath != null && protectedOnDeath instanceof Integer)
			return (Integer) protectedOnDeath;
		return -1;
	}
	
	public static void mapEquipIds() {
		int equipId = 0;
		for (int itemId = 0; itemId < Utils.getItemDefinitionsSize(); itemId++) {
			ItemDefinitions def = ItemDefinitions.getDefs(itemId);
			if (def.getMaleWornModelId1() >= 0 || def.getFemaleWornModelId1() >= 0) {
				EQUIP_IDS.put(itemId, equipId++);
			}
		}
	}
	
	public int getEquipId() {
		if (EQUIP_IDS.isEmpty())
			mapEquipIds();
		Integer equipId = EQUIP_IDS.get(id);
		return equipId == null ? -1 : equipId;
	}
	
	public static final ItemDefinitions getItemDefinitions(Store store, int itemId, boolean loadTemplates) {
		return new ItemDefinitions(store, itemId, loadTemplates);
	}
	
	public static final ItemDefinitions getItemDefinitions(Store store, int itemId) {
		return new ItemDefinitions(store, itemId, true);
	}
	
	public static final ItemDefinitions getDefs(int itemId) {
		return getItemDefinitions(itemId, true);
	}

	public static final ItemDefinitions getItemDefinitions(int itemId, boolean loadTemplates) {
		ItemDefinitions def = ITEM_DEFINITIONS.get(itemId);
		if (def == null) {
			def = new ItemDefinitions(itemId, loadTemplates);
			ITEM_DEFINITIONS.put(itemId, def);
		}
		return def;
	}

	public static final void clearItemsDefinitions() {
		ITEM_DEFINITIONS.clear();
	}
	
	public ItemDefinitions(int id) {
		this(Cache.STORE, id, true);
	}
	
	public ItemDefinitions(int id, boolean loadTemplates) {
		this(Cache.STORE, id, loadTemplates);
	}

	public ItemDefinitions(Store store, int id, boolean loadTemplates) {
		this.id = id;
		setDefaultOptions();
		loadItemDefinitions(store, loadTemplates);
		switch (id) {
		case 25629:
			this.name = "Iron";
			break;
		case 25630:
			this.name = "Coal";
			break;
		case 25631:
			this.name = "Mithril";
			break;
		case 25632:
			this.name = "Adamantite";
			break;
		case 25633:
			this.name = "Runite";
			break;
		default:
			break;
		}
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

	public boolean isLoaded() {
		return loaded;
	}

	private final void loadItemDefinitions(Store store, boolean loadTemplates) {
		byte[] data = store.getIndex(IndexType.ITEMS).getFile(ArchiveType.ITEMS.archiveId(id), ArchiveType.ITEMS.fileId(id));
		if (data == null) {
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (loadTemplates) {
			if (certTemplateId != -1)
				toNote();
			if (lendTemplateId != -1)
				toLend();
			if (bindTemplateId != -1)
				toBind();
		}
		parseBonuses();
		loaded = true;
	}

	public static final int STAB_ATTACK = 0, SLASH_ATTACK = 1, CRUSH_ATTACK = 2, RANGE_ATTACK = 4, MAGIC_ATTACK = 3;
	public static final int STAB_DEF = 5, SLASH_DEF = 6, CRUSH_DEF = 7, RANGE_DEF = 9, MAGIC_DEF = 8, SUMMONING_DEF = 10;
	public static final int STRENGTH_BONUS = 14, RANGED_STR_BONUS = 15, MAGIC_DAMAGE = 17, PRAYER_BONUS = 16;
	public static final int ABSORVE_MELEE_BONUS = 11, ABSORVE_RANGE_BONUS = 13, ABSORVE_MAGE_BONUS = 12;
	
	public boolean faceMask() {
		if (id == 4168)
			return true;
		return getParamVal(625) == 1;
	}

	private void parseBonuses() {
		bonuses = new int[18];
		bonuses[STAB_ATTACK] = getParamVal(0);
		bonuses[SLASH_ATTACK] = getParamVal(1);
		bonuses[CRUSH_ATTACK] = getParamVal(2);
		bonuses[MAGIC_ATTACK] = getParamVal(3);
		bonuses[RANGE_ATTACK] = getParamVal(4);
		bonuses[STAB_DEF] = getParamVal(5);
		bonuses[SLASH_DEF] = getParamVal(6);
		bonuses[CRUSH_DEF] = getParamVal(7);
		bonuses[MAGIC_DEF] = getParamVal(8);
		bonuses[RANGE_DEF] = getParamVal(9);
		bonuses[SUMMONING_DEF] = getParamVal(417);
		bonuses[PRAYER_BONUS] = getParamVal(11);

		bonuses[ABSORVE_MELEE_BONUS] = getParamVal(967);
		bonuses[ABSORVE_RANGE_BONUS] = getParamVal(968);
		bonuses[ABSORVE_MAGE_BONUS] = getParamVal(969);

		bonuses[STRENGTH_BONUS] = getParamVal(641) / 10;
		bonuses[RANGED_STR_BONUS] = getParamVal(643) / 10;
		bonuses[MAGIC_DAMAGE] = getParamVal(685);
		
		if (id == 25349) {
			for (int i = 0;i < bonuses.length;i++) {
				bonuses[i] = 10000;
			}
		}
	}
	

	public void setBonuses(int[] bonuses2) {
		if (bonuses2[STAB_ATTACK] != 0 || bonuses[STAB_ATTACK] != bonuses2[STAB_ATTACK])
			clientScriptData.put(0, bonuses2[STAB_ATTACK]);
		if (bonuses2[SLASH_ATTACK] != 0 || bonuses[SLASH_ATTACK] != bonuses2[SLASH_ATTACK])
			clientScriptData.put(1, bonuses2[SLASH_ATTACK]);
		if (bonuses2[CRUSH_ATTACK] != 0 || bonuses[CRUSH_ATTACK] != bonuses2[CRUSH_ATTACK])
			clientScriptData.put(2, bonuses2[CRUSH_ATTACK]);
		if (bonuses2[MAGIC_ATTACK] != 0 || bonuses[MAGIC_ATTACK] != bonuses2[MAGIC_ATTACK])
			clientScriptData.put(3, bonuses2[MAGIC_ATTACK]);
		if (bonuses2[RANGE_ATTACK] != 0 || bonuses[RANGE_ATTACK] != bonuses2[RANGE_ATTACK])
			clientScriptData.put(4, bonuses2[RANGE_ATTACK]);
		if (bonuses2[STAB_DEF] != 0 || bonuses[STAB_DEF] != bonuses2[STAB_DEF])
			clientScriptData.put(5, bonuses2[STAB_DEF]);
		if (bonuses2[SLASH_DEF] != 0 || bonuses[SLASH_DEF] != bonuses2[SLASH_DEF])
			clientScriptData.put(6, bonuses2[SLASH_DEF]);
		if (bonuses2[CRUSH_DEF] != 0 || bonuses[CRUSH_DEF] != bonuses2[CRUSH_DEF])
			clientScriptData.put(7, bonuses2[CRUSH_DEF]);
		if (bonuses2[MAGIC_DEF] != 0 || bonuses[MAGIC_DEF] != bonuses2[MAGIC_DEF])
			clientScriptData.put(8, bonuses2[MAGIC_DEF]);
		if (bonuses2[RANGE_DEF] != 0 || bonuses[RANGE_DEF] != bonuses2[RANGE_DEF])
			clientScriptData.put(9, bonuses2[RANGE_DEF]);
		if (bonuses2[SUMMONING_DEF] != 0 || bonuses[SUMMONING_DEF] != bonuses2[SUMMONING_DEF])
			clientScriptData.put(417, bonuses2[SUMMONING_DEF]);
		if (bonuses2[PRAYER_BONUS] != 0 || bonuses[PRAYER_BONUS] != bonuses2[PRAYER_BONUS])
			clientScriptData.put(11, bonuses2[PRAYER_BONUS]);
		if (bonuses2[ABSORVE_MELEE_BONUS] != 0 || bonuses[ABSORVE_MELEE_BONUS] != bonuses2[ABSORVE_MELEE_BONUS])
			clientScriptData.put(967, bonuses2[ABSORVE_MELEE_BONUS]);
		if (bonuses2[ABSORVE_RANGE_BONUS] != 0 || bonuses[ABSORVE_RANGE_BONUS] != bonuses2[ABSORVE_RANGE_BONUS])
			clientScriptData.put(968, bonuses2[ABSORVE_RANGE_BONUS]);
		if (bonuses2[ABSORVE_MAGE_BONUS] != 0 || bonuses[ABSORVE_MAGE_BONUS] != bonuses2[ABSORVE_MAGE_BONUS])
			clientScriptData.put(969, bonuses2[ABSORVE_MAGE_BONUS]);
		if (bonuses2[STRENGTH_BONUS] != 0 || bonuses[STRENGTH_BONUS] != bonuses2[STRENGTH_BONUS])
			clientScriptData.put(641, bonuses2[STRENGTH_BONUS] * 10);
		if (bonuses2[RANGED_STR_BONUS] != 0 || bonuses[RANGED_STR_BONUS] != bonuses2[RANGED_STR_BONUS])
			clientScriptData.put(643, bonuses2[RANGED_STR_BONUS] * 10);
		if (bonuses2[MAGIC_DAMAGE] != 0 || bonuses[MAGIC_DAMAGE] != bonuses2[MAGIC_DAMAGE])
			clientScriptData.put(685, bonuses2[MAGIC_DAMAGE]);
	}

	private void toNote() {
		// ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getDefs(certId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
		noted = true;
	}

	private void toBind() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getDefs(bindId);
		originalModelColors = realItem.originalModelColors;
		maleEquip3 = realItem.maleEquip3;
		femaleEquip3 = realItem.femaleEquip3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		clientScriptData = realItem.clientScriptData;
		wearPos = realItem.wearPos;
		wearPos2 = realItem.wearPos2;
	}

	private void toLend() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getDefs(lendId);
		originalModelColors = realItem.originalModelColors;
		maleEquip3 = realItem.maleEquip3;
		femaleEquip3 = realItem.femaleEquip3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		clientScriptData = realItem.clientScriptData;
		wearPos = realItem.wearPos;
		wearPos2 = realItem.wearPos2;
		lended = true;
	}

	public boolean isDestroyItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("destroy"))
				return true;
		}
		return false;
	}
	
	public boolean canExchange() {
		if (isNoted() && certId != -1)
			return getDefs(certId).grandExchange;
		return grandExchange;
	}

	public int getStageOnDeath() {
		if (clientScriptData == null)
			return 0;
		Object protectedOnDeath = clientScriptData.get(1397);
		if (protectedOnDeath != null && protectedOnDeath instanceof Integer)
			return (Integer) protectedOnDeath;
		return 0;
	}

	public boolean containsOption(int i, String option) {
		if (inventoryOptions == null || inventoryOptions[i] == null || inventoryOptions.length <= i)
			return false;
		return inventoryOptions[i].equals(option);
	}

	public boolean containsOption(String option) {
		if (inventoryOptions == null)
			return false;
		for (String o : inventoryOptions) {
			if (o == null || !o.equals(option))
				continue;
			return true;
		}
		return false;
	}

	public boolean isWearItem() {
		return wearPos != -1;
	}

	public boolean isWearItem(boolean male) {
		if (id == 4285)
			return true;
		if (wearPos < 12 && (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1))
			return false;
		return wearPos != -1;
	}

	public boolean hasSpecialBar() {
		if (clientScriptData == null)
			return false;
		Object specialBar = clientScriptData.get(686);
		if (specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar == 1;
		return false;
	}

	public int getRenderAnimId() {
		if (clientScriptData == null)
			return 1426;
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer)
			return (Integer) animId;
		return 1426;
	}

	public double getDungShopValueMultiplier() {
		if (clientScriptData == null)
			return 1;
		Object value = clientScriptData.get(1046);
		if (value != null && value instanceof Integer)
			return ((Integer) value).doubleValue() / 100;
		return 1;
	}

	public int getModelZoom() {
		return modelZoom;
	}

	public int getModelOffset1() {
		return modelOffsetX;
	}

	public int getModelOffset2() {
		return modelOffsetY;
	}

	public int getQuestId() {
		if (clientScriptData == null)
			return -1;
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}

	public int getWieldQuestReq() {
		return getCS2Var(-1, 743);
	}
	
	public int getParamVal(int id) {
		return getCS2Var(0, id);
	}
	
	public int getCS2Var(int defaultVal, int id) {
		if (clientScriptData == null || clientScriptData.get(id) == null)
			return defaultVal;
		return (int) clientScriptData.get(id);
	}

	public HashMap<Integer, Object> getClientScriptData() {
		return clientScriptData;
	}

	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
		if (clientScriptData == null)
			return null;
		if (itemRequiriments == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) clientScriptData.get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) clientScriptData.get(750 + (i * 2));
					if (level != null)
						skills.put(skill, level);
				}
			}
			Integer maxedSkill = (Integer) clientScriptData.get(277);
			if (maxedSkill != null)
				skills.put(maxedSkill, getId() == 19709 ? 120 : 99);
			itemRequiriments = skills;
			switch(getId()) {
			case 8846:
				itemRequiriments.put(Constants.ATTACK, 5);
				itemRequiriments.put(Constants.DEFENSE, 5);
				break;
			case 8847:
				itemRequiriments.put(Constants.ATTACK, 10);
				itemRequiriments.put(Constants.DEFENSE, 10);
				break;
			case 8848:
				itemRequiriments.put(Constants.ATTACK, 20);
				itemRequiriments.put(Constants.DEFENSE, 20);
				break;
			case 8849:
				itemRequiriments.put(Constants.ATTACK, 30);
				itemRequiriments.put(Constants.DEFENSE, 30);
				break;
			case 8850:
				itemRequiriments.put(Constants.ATTACK, 40);
				itemRequiriments.put(Constants.DEFENSE, 40);
				break;
			case 20072:
				itemRequiriments.put(Constants.ATTACK, 60);
				itemRequiriments.put(Constants.DEFENSE, 60);
				break;
			case 10498:
				itemRequiriments.put(Constants.RANGE, 30);
				break;
			case 10499:
				itemRequiriments.put(Constants.RANGE, 50);
				break;
			case 20068:
				itemRequiriments.put(Constants.RANGE, 50);
				break;
			case 22358:
			case 22359:
			case 22360:
			case 22361:
				itemRequiriments.put(Constants.ATTACK, 80);
				break;
			case 22362:
			case 22363:
			case 22364:
			case 22365:
				itemRequiriments.put(Constants.RANGE, 80);
				break;
			case 22366:
			case 22367:
			case 22368:
			case 22369:
				itemRequiriments.put(Constants.MAGIC, 80);
				break;
			}
		}

		return itemRequiriments;
	}

	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}
	
	public boolean write(Store store) {
		return store.getIndex(IndexType.ITEMS).putFile(ArchiveType.ITEMS.archiveId(id), ArchiveType.ITEMS.fileId(id), encode());
	}
	
	private final byte[] encode() {
		OutputStream stream = new OutputStream();
		
		stream.writeByte(1);
		stream.writeBigSmart(modelId);
		
		if (!name.equals("null")) {
			stream.writeByte(2);
			stream.writeString(name);
		}
		
		if (modelZoom != 2000) {
			stream.writeByte(4);
			stream.writeShort(modelZoom);
		}
		
		if (modelRotationX != 0) {
			stream.writeByte(5);
			stream.writeShort(modelRotationX);
		}
		
		if (modelRotationY != 0) {
			stream.writeByte(6);
			stream.writeShort(modelRotationY);
		}
		
		if (modelOffsetX != 0) {
			stream.writeByte(7);
			int value = modelOffsetX >>= 0;
			if (value < 0)
				value += 65536;
			stream.writeShort(value);
		}

		if (modelOffsetY != 0) {
			stream.writeByte(8);
			int value = modelOffsetY >>= 0;
			if (value < 0)
				value += 65536;
			stream.writeShort(value);
		}
		
		if (stackable == 1) {
			stream.writeByte(11);
		}
		
		if (value != 1) {
			stream.writeByte(12);
			stream.writeInt(value);
		}
		
		if (wearPos != -1) {
			stream.writeByte(13);
			stream.writeByte(wearPos);
		}
		
		if (wearPos2 != -1) {
			stream.writeByte(14);
			stream.writeByte(wearPos2);
		}
		
		if (membersOnly) {
			stream.writeByte(16);
		}
		
		if (multiStackSize != -1) {
			stream.writeByte(18);
			stream.writeShort(multiStackSize);
		}
		
		if (maleEquip1 != -1) {
			stream.writeByte(23);
			stream.writeBigSmart(maleEquip1);
		}
		
		if (maleEquip2 != -1) {
			stream.writeByte(24);
			stream.writeBigSmart(maleEquip2);
		}
		
		if (femaleEquip1 != -1) {
			stream.writeByte(25);
			stream.writeBigSmart(femaleEquip1);
		}
		
		if (femaleEquip2 != -1) {
			stream.writeByte(26);
			stream.writeBigSmart(femaleEquip2);
		}
		
		if (wearPos3 != -1) {
			stream.writeByte(27);
			stream.writeByte(wearPos3);
		}
		
		String[] DEFAULTGROUND = new String[] { null, null, "take", null, null };
		for (int i = 0;i < groundOptions.length;i++) {
			if (groundOptions[i] != null && !groundOptions[i].equals(DEFAULTGROUND[i])) {
				stream.writeByte(30+i);
				stream.writeString(groundOptions[i]);
			}
		}
		
		String[] DEFAULTINV = new String[] { null, null, null, null, "drop" };
		for (int i = 0;i < inventoryOptions.length;i++) {
			if (inventoryOptions[i] != null && !inventoryOptions[i].equals(DEFAULTINV[i])) {
				stream.writeByte(35+i);
				stream.writeString(inventoryOptions[i]);
			}
		}
		
		if (originalModelColors != null && modifiedModelColors != null) {
			stream.writeByte(40);
			stream.writeByte(originalModelColors.length);
			for (int i = 0; i < originalModelColors.length; i++) {
				stream.writeShort(originalModelColors[i]);
				stream.writeShort(modifiedModelColors[i]);
			}
		}

		if (originalTextureIds != null && modifiedTextureIds != null) {
			stream.writeByte(41);
			stream.writeByte(originalTextureIds.length);
			for (int i = 0; i < originalTextureIds.length; i++) {
				stream.writeShort(originalTextureIds[i]);
				stream.writeShort(modifiedTextureIds[i]);
			}
		}
		
		if (spriteRecolorIndices != null) {
			stream.writeByte(42);
			stream.writeByte(spriteRecolorIndices.length);
			for (int i = 0; i < spriteRecolorIndices.length; i++)
				stream.writeByte(spriteRecolorIndices[i]);
		}

		if (tooltipColor != 0 && hasTooltipColor) {
			stream.writeByte(43);
			stream.writeInt(tooltipColor);
		}
		
		if (grandExchange) {
			stream.writeByte(65);
		}

		if (maleEquip3 != -1) {
			stream.writeByte(78);
			stream.writeBigSmart(maleEquip3);
		}

		if (femaleEquip3 != -1) {
			stream.writeByte(79);
			stream.writeBigSmart(femaleEquip3);
		}

		if (maleHead1 != -1) {
			stream.writeByte(90);
			stream.writeBigSmart(maleHead1);
		}
		
		if (femaleHead1 != -1) {
			stream.writeByte(91);
			stream.writeBigSmart(femaleHead1);
		}

		if (maleHead2 != -1) {
			stream.writeByte(92);
			stream.writeBigSmart(maleHead2);
		}

		if (femaleHead2 != -1) {
			stream.writeByte(93);
			stream.writeBigSmart(femaleHead2);
		}
		
		if (modelRotationZ != 0) {
			stream.writeByte(95);
			stream.writeShort(modelRotationZ);
		}
		
		if (unknownInt6 != 0) {
			stream.writeByte(96);
			stream.writeByte(unknownInt6);
		}

		if (certId != -1) {
			stream.writeByte(97);
			stream.writeShort(certId);
		}

		if (certTemplateId != -1) {
			stream.writeByte(98);
			stream.writeShort(certTemplateId);
		}
		
		if (stackIds != null && stackAmounts != null) {
			for (int i = 0; i < stackIds.length; i++) {
				if (stackIds[i] == 0 && stackAmounts[i] == 0)
					continue;
				stream.writeByte(100 + i);
				stream.writeShort(stackIds[i]);
				stream.writeShort(stackAmounts[i]);
			}
		}

		if (resizeX != 128) {
			stream.writeByte(110);
			stream.writeShort(resizeX);
		}

		if (resizeY != 128) {
			stream.writeByte(111);
			stream.writeShort(resizeY);
		}
		
		if (resizeZ != 128) {
			stream.writeByte(112);
			stream.writeShort(resizeZ);
		}

		if (ambient != 0) {
			stream.writeByte(113);
			stream.writeByte(ambient);
		}

		if (contrast != 0) {
			stream.writeByte(114);
			stream.writeByte(contrast / 5);
		}

		if (teamId != 0) {
			stream.writeByte(115);
			stream.writeByte(teamId);
		}

		if (lendId != -1) {
			stream.writeByte(121);
			stream.writeShort(lendId);
		}

		if (lendTemplateId != -1) {
			stream.writeByte(122);
			stream.writeShort(lendTemplateId);
		}
		
		if (maleWearXOffset != 0 || maleWearYOffset != 0 || maleWearZOffset != 0) {
			stream.writeByte(125);
			stream.writeByte(maleWearXOffset >> 2);
			stream.writeByte(maleWearYOffset >> 2);
			stream.writeByte(maleWearZOffset >> 2);
		}

		if (femaleWearXOffset != 0 || femaleWearYOffset != 0 || femaleWearZOffset != 0) {
			stream.writeByte(126);
			stream.writeByte(femaleWearXOffset >> 2);
			stream.writeByte(femaleWearYOffset >> 2);
			stream.writeByte(femaleWearZOffset >> 2);
		}
		
		if (unknownInt18 != -1 || unknownInt19 != -1) {
			stream.writeByte(127);
			stream.writeByte(unknownInt18);
			stream.writeShort(unknownInt19);
		}
		
		if (unknownInt20 != -1 || unknownInt21 != -1) {
			stream.writeByte(128);
			stream.writeByte(unknownInt20);
			stream.writeShort(unknownInt21);
		}
		
		if (customCursorOp1 != -1 || customCursorId1 != -1) {
			stream.writeByte(129);
			stream.writeByte(customCursorOp1);
			stream.writeShort(customCursorId1);
		}

		if (customCursorOp2 != -1 || customCursorId2 != -1) {
			stream.writeByte(130);
			stream.writeByte(customCursorOp2);
			stream.writeShort(customCursorId2);
		}
		
		if (quests != null) {
			stream.writeByte(132);
			stream.writeByte(quests.length);
			for (int index = 0; index < quests.length; index++)
				stream.writeShort(quests[index]);
		}

		if (pickSizeShift != 0) {
			stream.writeByte(134);
			stream.writeByte(pickSizeShift);
		}
		
		if (bindId != -1) {
			stream.writeByte(139);
			stream.writeShort(bindId);
		}
		
		if (bindTemplateId != -1) {
			stream.writeByte(140);
			stream.writeShort(bindTemplateId);
		}
		
		if (clientScriptData != null) {
			stream.writeByte(249);
			stream.writeByte(clientScriptData.size());
			for (int key : clientScriptData.keySet()) {
				Object value = clientScriptData.get(key);
				stream.writeByte(value instanceof String ? 1 : 0);
				stream.write24BitInt(key);
				if (value instanceof String) {
					stream.writeString((String) value);
				} else {
					stream.writeInt((Integer) value);
				}
			}
		}
		stream.writeByte(0);
		
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}

	@SuppressWarnings("unused")
	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			modelId = stream.readBigSmart();
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 4)
			modelZoom = stream.readUnsignedShort();
		else if (opcode == 5)
			modelRotationX = stream.readUnsignedShort();
		else if (opcode == 6)
			modelRotationY = stream.readUnsignedShort();
		else if (opcode == 7) {
			modelOffsetX = stream.readUnsignedShort();
			if (modelOffsetX > 32767)
				modelOffsetX -= 65536;
			modelOffsetX <<= 0;
		} else if (opcode == 8) {
			modelOffsetY = stream.readUnsignedShort();
			if (modelOffsetY > 32767)
				modelOffsetY -= 65536;
			modelOffsetY <<= 0;
		} else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12) {
			value = stream.readInt();
		} else if (opcode == 13) {
			wearPos = stream.readUnsignedByte();
		} else if (opcode == 14) {
			wearPos2 = stream.readUnsignedByte();
		} else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 18) {
			multiStackSize = stream.readUnsignedShort();
		} else if (opcode == 23)
			maleEquip1 = stream.readBigSmart();
		else if (opcode == 24)
			maleEquip2 = stream.readBigSmart();
		else if (opcode == 25)
			femaleEquip1 = stream.readBigSmart();
		else if (opcode == 26)
			femaleEquip2 = stream.readBigSmart();
		else if (opcode == 27)
			wearPos3 = stream.readUnsignedByte();
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode - 30] = stream.readString();
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode - 35] = stream.readString();
		else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = stream.readUnsignedShort();
				modifiedModelColors[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureIds = new int[length];
			modifiedTextureIds = new int[length];
			for (int index = 0; index < length; index++) {
				originalTextureIds[index] = (short) stream.readUnsignedShort();
				modifiedTextureIds[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			spriteRecolorIndices = new byte[length];
			for (int index = 0; index < length; index++)
				spriteRecolorIndices[index] = (byte) stream.readByte();
		} else if (opcode == 43) {
			this.tooltipColor = stream.readInt();
			this.hasTooltipColor = true;
		} else if (opcode == 44) {
			/*int i_7_ = */stream.readUnsignedShort();
//		    int i_8_ = 0;
//		    for (int i_9_ = i_7_; i_9_ > 0; i_9_ >>= 1)
//			i_8_++;
//		    aByteArray6974 = new byte[i_8_];
//		    byte i_10_ = 0;
//		    for (int i_11_ = 0; i_11_ < i_8_; i_11_++) {
//			if ((i_7_ & 1 << i_11_) > 0) {
//			    aByteArray6974[i_11_] = i_10_;
//			    i_10_++;
//			} else
//			    aByteArray6974[i_11_] = (byte) -1;
//		    }
		} else if (45 == opcode) {
		    /*int i_12_ = */stream.readUnsignedShort();
//		    int i_13_ = 0;
//		    for (int i_14_ = i_12_; i_14_ > 0; i_14_ >>= 1)
//			i_13_++;
//		   aByteArray6975 = new byte[i_13_];
//		    byte i_15_ = 0;
//		    for (int i_16_ = 0; i_16_ < i_13_; i_16_++) {
//			if ((i_12_ & 1 << i_16_) > 0) {
//			    aByteArray6975[i_16_] = i_15_;
//			    i_15_++;
//			} else
//			   aByteArray6975[i_16_] = (byte) -1;
//		    }
		} else if (opcode == 65)
			grandExchange = true;
		else if (opcode == 78)
			maleEquip3 = stream.readBigSmart();
		else if (opcode == 79)
			femaleEquip3 = stream.readBigSmart();
		else if (opcode == 90)
			maleHead1 = stream.readBigSmart();
		else if (opcode == 91)
			femaleHead1 = stream.readBigSmart();
		else if (opcode == 92)
			maleHead2 = stream.readBigSmart();
		else if (opcode == 93)
			femaleHead2 = stream.readBigSmart();
		else if (opcode == 94) // new
			stream.readUnsignedShort();
		else if (opcode == 95)
			modelRotationZ = stream.readUnsignedShort();
		else if (opcode == 96)
			unknownInt6 = stream.readUnsignedByte();
		else if (opcode == 97)
			certId = stream.readUnsignedShort();
		else if (opcode == 98)
			certTemplateId = stream.readUnsignedShort();
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110)
			resizeX = stream.readUnsignedShort();
		else if (opcode == 111)
			resizeY = stream.readUnsignedShort();
		else if (opcode == 112)
			resizeZ = stream.readUnsignedShort();
		else if (opcode == 113)
			ambient = stream.readByte();
		else if (opcode == 114)
			contrast = stream.readByte() * 5;
		else if (opcode == 115)
			teamId = stream.readUnsignedByte();
		else if (opcode == 121)
			lendId = stream.readUnsignedShort();
		else if (opcode == 122)
			lendTemplateId = stream.readUnsignedShort();
		else if (opcode == 125) {
			this.maleWearXOffset = stream.readByte() << 2;
			this.maleWearYOffset = stream.readByte() << 2;
			this.maleWearZOffset = stream.readByte() << 2;
		} else if (opcode == 126) {
			this.femaleWearXOffset = stream.readByte() << 2;
			this.femaleWearYOffset = stream.readByte() << 2;
			this.femaleWearZOffset = stream.readByte() << 2;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			customCursorOp1 = stream.readUnsignedByte();
			customCursorId1 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			customCursorOp2 = stream.readUnsignedByte();
			customCursorId2 = stream.readUnsignedShort();
		} else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			quests = new int[length];
			for (int index = 0; index < length; index++)
				quests[index] = stream.readUnsignedShort();
		} else if (opcode == 134) {
			this.pickSizeShift = stream.readUnsignedByte();
		} else if (opcode == 139) {
			bindId = stream.readUnsignedShort();
		} else if (opcode == 140) {
			bindTemplateId = stream.readUnsignedShort();
		} else if (opcode >= 142 && opcode < 147) {
//			if (null == ((Class518) this).anIntArray6988) {
//				((Class518) this).anIntArray6988 = new int[6];
//				Arrays.fill(((Class518) this).anIntArray6988, -1);
//			}
			/*anIntArray6988[opcode - 142] = */stream.readUnsignedShort();
		} else if (opcode >= 150 && opcode < 155) {
//			if (null == ((Class518) this).anIntArray6986) {
//				((Class518) this).anIntArray6986 = new int[5];
//				Arrays.fill(((Class518) this).anIntArray6986, -1);
//			}
			/*anIntArray6986[opcode - 150] = */stream.readUnsignedShort();
		} else if (opcode == 157) {
			
		} else if (161 == opcode) {// new
			int anInt7904 = stream.readUnsignedShort();
		} else if (162 == opcode) {// new
			int anInt7923 = stream.readUnsignedShort();
		} else if (163 == opcode) {// new
			int anInt7939 = stream.readUnsignedShort();
		} else if (164 == opcode) {// new coinshare shard
			String aString7902 = stream.readString();
		} else if (165 == opcode) {// new
			stackable = 2;
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (clientScriptData == null)
				clientScriptData = new HashMap<Integer, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				clientScriptData.put(key, value);
			}
		} else {
			System.err.println("MISSING OPCODE " + opcode + " FOR ITEM " + getId());
			throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + getId());
		}
	}

	private final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public boolean containsEquipmentOption(int optionId, String option) {
		if (clientScriptData == null)
			return false;
		Object wearingOption = clientScriptData.get(528 + optionId);
		if (wearingOption != null && wearingOption instanceof String)
			return wearingOption.equals(option);
		return false;
	}

	public String getEquipmentOption(int optionId) {
		if (clientScriptData == null)
			return "null";
		Object wearingOption = clientScriptData.get(optionId == 4 ? 1211 : (528 + optionId));
		if (wearingOption != null && wearingOption instanceof String)
			return (String) wearingOption;
		return "null";
	}

	public String getInventoryOption(int optionId) {
		switch(id) {
		case 6099:
		case 6100:
		case 6101:
		case 6102:
			if (optionId == 2)
				return "Temple";
			break;
		case 19760:
		case 13561:
		case 13562:
			if (optionId == 0)
				return inventoryOptions[1];
			else if (optionId == 1)
				return inventoryOptions[0];
			break;
		}
		if (inventoryOptions == null)
			return "null";
		if (optionId >= inventoryOptions.length)
			return "null";
		if (inventoryOptions[optionId] == null)
			return "null";
		return inventoryOptions[optionId];
	}

	public String getName() {
		return name;
	}

	public int getFemaleWornModelId1() {
		return femaleEquip1;
	}

	public int getFemaleWornModelId2() {
		return femaleEquip2;
	}

	public int getMaleWornModelId1() {
		return maleEquip1;
	}

	public int getMaleWornModelId2() {
		return maleEquip2;
	}

	public boolean isOverSized() {
		return modelZoom > 5000;
	}

	public boolean isLended() {
		return lended;
	}

	public boolean isMembersOnly() {
		return membersOnly;
	}

	public boolean isStackable() {
		return stackable == 1;
	}

	public boolean isNoted() {
		return noted;
	}

	public int getLendId() {
		return lendId;
	}

	public int getCertId() {
		return certId;
	}

	public int getValue() {
		if (id == 18744 || id == 18745 || id == 18746 || id == 18747)
			return 5;

		if (id == 10551 || id == 10548 || id == 2952 || id == 2890 || id == 10499)
			return 1;
		return value;
	}

	public int getSellPrice() {
		return (int) (value / (10.0 / 3.0));
	}

	public int getHighAlchPrice() {
		return (int) (value / (10.0 / 6.0));
	}

	public int getId() {
		return id;
	}

	public int getEquipSlot() {
		return wearPos;
	}
	
	public boolean isEquipType(int type) {
		return wearPos3 == type || wearPos2 == type;
	}

	public List<Item> getCreateItemRequirements(boolean infusingScroll) {
		if (clientScriptData == null) {
			return null;
		}
		List<Item> items = new ArrayList<Item>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String) {
				continue;
			}
			if (key >= 536 && key <= 770) {
				if (key % 2 == 0) {
					requiredId = (Integer) value;
				} else {
					requiredAmount = (Integer) value;
				}
				if (requiredId != -1 && requiredAmount != -1) {
					if (infusingScroll) {
						requiredId = getId();
						requiredAmount = 1;
					}
					if (items.size() == 0 && !infusingScroll) {
						items.add(new Item(requiredAmount, 1));
					} else {
						items.add(new Item(requiredId, requiredAmount));
					}
					requiredId = -1;
					requiredAmount = -1;
					if (infusingScroll) {
						break;
					}
				}
			}
		}
		return items;
	}

	public HashMap<Integer, Integer> getCreateItemRequirements() {
		if (clientScriptData == null) {
			return null;
		}
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String) {
				continue;
			}
			if (key >= 538 && key <= 770) {
				if (key % 2 == 0) {
					requiredId = (Integer) value;
				} else {
					requiredAmount = (Integer) value;
				}
				if (requiredId != -1 && requiredAmount != -1) {
					items.put(requiredAmount, requiredId);
					requiredId = -1;
					requiredAmount = -1;
				}
			}
		}
		return items;
	}

	public boolean isBinded() {
		return (id >= 15775 && id <= 16272) || (id >= 19865 && id <= 19866);
	}

	public boolean isBindItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("bind"))
				return true;
		}
		return false;
	}

	public boolean containsInventoryOption(int i, String string) {
		if (inventoryOptions == null || i >= inventoryOptions.length || inventoryOptions[i] == null)
			return false;
		return inventoryOptions[i].equalsIgnoreCase(string);
	}

	public int[] getBonuses() {
		return bonuses;
	}

}