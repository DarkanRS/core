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
package com.rs.lib.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cache.loaders.model.RSModel;
import com.rs.cache.loaders.model.TextureConverter;
import com.rs.lib.util.Utils;

public class ItemDefEditor {
	
	private static Store NEW;
	private static Map<Integer, Integer> PACKED_MAP = new HashMap<>();
	private static Map<Integer, Integer> ID_MAP = new HashMap<>();
			
	public static final void main(String[] args) throws IOException {
		//Cache.init();
		NEW = new Store("D:/RSPS/876cache/", true);
		
//		ItemDefinitions dungCape = ItemDefinitions.getDefs(19709);
//		for (int i = 25324; i <= 25348; i++) {
//			ItemDefinitions def = ItemDefinitions.getItemDefinitions(i, false);
//			def.setBonuses(dungCape.getBonuses());
//			System.out.println("Writing " + def.getName());
//			def.write(Cache.STORE);
//		}

//		for (int i = 31851;i <= 31866;i++)
//			packItem(NEW, Cache.STORE, i, true);
//		for (int i = 31869;i <= 31880;i++)
//			packItem(NEW, Cache.STORE, i, true);
//		for (int i = 32151;i <= 32153;i++)
//			packItem(NEW, Cache.STORE, i, true);
//		for (int i = 39322;i <= 39387;i++)
//			packItem(NEW, Cache.STORE, i, true);
		
//		RSModel model = RSModel.getMesh(ItemDefinitions.getItemDefinitions(20771, false).getMaleWornModelId1());
//		if (model.emitterConfigs != null)
//			System.out.println(ParticleProducerDefinitions.getDefs(model.emitterConfigs[0].type));
		
//		ItemDefinitions cape = ItemDefinitions.getItemDefinitions(NEW, 31268, false);
//		RSModel model = RSModel.getMesh(NEW, cape.getMaleWornModelId1(), true);
//		byte[] buffer = model.convertTo727(Cache.STORE, NEW, false);
//		RSModel converted = new RSModel(buffer);
		
//		System.err.println("COMPARE");
//		System.out.println(model.version + " -> " + converted.version);
//		System.out.println(model.faceCount + " -> " + converted.faceCount);
//		System.out.println(model.texturedFaceCount + " -> " + converted.texturedFaceCount);
//		System.out.println(model.vertexCount + " -> " + converted.vertexCount);
//		System.out.println(model.maxVertexUsed + " -> " + converted.maxVertexUsed);
//		System.out.println(model.emitterConfigs + ", " + converted.emitterConfigs);
//		System.out.println("textureRenderTypes: " + Arrays.equals(model.textureRenderTypes, converted.textureRenderTypes));
//		System.out.println("vertexX: " + Arrays.equals(model.vertexX, converted.vertexX));
//		System.out.println("vertexY: " + Arrays.equals(model.vertexY, converted.vertexY));
//		System.out.println("vertexZ: " + Arrays.equals(model.vertexZ, converted.vertexZ));
//		System.out.println("vertexBones: " + Arrays.equals(model.vertexBones, converted.vertexBones));
//		System.out.println("faceColors: " + Arrays.equals(model.faceTextures, converted.faceTextures));
//		System.out.println("faceRenderTypes: " + Arrays.equals(model.faceRenderTypes, converted.faceRenderTypes));
//		System.out.println("facePriorities: " + Arrays.equals(model.facePriorities, converted.facePriorities));
//		System.out.println("faceAlpha: " + Arrays.equals(model.faceAlpha, converted.faceAlpha));
//		System.out.println("faceBones: " + Arrays.equals(model.faceBones, converted.faceBones));
//		System.out.println("faceTextures: " + Arrays.equals(model.faceTextures, converted.faceTextures));
//		System.out.println("faceTextureIndexes: " + Arrays.equals(model.faceTextureIndexes, converted.faceTextureIndexes));
//
//		System.out.println("faceA: " + Arrays.equals(model.faceA, converted.faceA));
//		System.out.println("faceB: " + Arrays.equals(model.faceB, converted.faceB));
//		System.out.println("faceC: " + Arrays.equals(model.faceC, converted.faceC));
//		
//		System.out.println("textureTriangleX: " + Arrays.equals(model.textureTriangleX, converted.textureTriangleX));
//		System.out.println("textureTriangleY: " + Arrays.equals(model.textureTriangleY, converted.textureTriangleY));
//		System.out.println("textureTriangleZ: " + Arrays.equals(model.textureTriangleZ, converted.textureTriangleZ));
//		
//		System.out.println("textureScaleX: " + Arrays.equals(model.textureScaleX, converted.textureScaleX));
//		System.out.println("textureScaleY: " + Arrays.equals(model.textureScaleY, converted.textureScaleY));
//		System.out.println("textureScaleZ: " + Arrays.equals(model.textureScaleZ, converted.textureScaleZ));
	}
	
	public static boolean packItem(Store from, Store to, int itemId, boolean rs3) throws IOException {
		int newId = Utils.getItemDefinitionsSize();
		ItemDefinitions def = ItemDefinitions.getItemDefinitions(from, itemId, false);
		packModels(from, to, def, rs3);
		ID_MAP.put(itemId, newId);
		def.id = newId;
		if (def.certId != -1) {
			if (def.certTemplateId != -1)
				def.certId = ID_MAP.get(def.certId);
			else
				def.certId = newId+1;
		}
		if (def.lendId != -1) {
			if (def.lendTemplateId != -1)
				def.lendId = ID_MAP.get(def.lendId);
			else
				def.lendId = newId+2;
		}
		if (def.write(Cache.STORE))
			System.out.println("Packed: " + def.name + " to ID " + newId);
		else
			System.err.println("Error packing item " + itemId);
		return true;
	}
	
	public static boolean packTexture(Store cache, int id) {
		return cache.getIndex(IndexType.MATERIALS).putFile(id, 0, NEW.getIndex(IndexType.MATERIALS).getFile(id, 0));
	}

	public static int packModel(Store from, Store to, int modelId, boolean rs3) throws IOException {
		if (modelId == -1)
			return -1;
		if (modelId == 0)
			return 0;
		if (RSModel.getMesh(modelId) != null)
			return modelId;
		if (PACKED_MAP.get(modelId) != null)
			return PACKED_MAP.get(modelId);
		int archiveId = to.getIndex(IndexType.MODELS).getLastArchiveId()+1;
		System.out.println("Packing model: " + modelId + " to " + archiveId);
		if (rs3) {
			RSModel model = RSModel.getMesh(from, modelId, rs3);
			if (to.getIndex(IndexType.MODELS).putFile(archiveId, 0, model.convertTo727(to, from)))
				return archiveId;
		} else {
			PACKED_MAP.put(modelId, archiveId);
			if (to.getIndex(IndexType.MODELS).putFile(archiveId, 0, from.getIndex(IndexType.MODELS).getFile(modelId, 0)))
				return archiveId;
		}
		throw new RuntimeException();
	}
	
	public static void packModels(Store from, Store to, ItemDefinitions def, boolean rs3) throws IOException {
		def.modelId = packModel(from, to, def.modelId, rs3);
		def.maleEquip1 = packModel(from, to, def.maleEquip1, rs3);
		def.maleEquip2 = packModel(from, to, def.maleEquip2, rs3);
		def.maleEquip3 = packModel(from, to, def.maleEquip3, rs3);
		def.femaleEquip1 = packModel(from, to, def.femaleEquip1, rs3);
		def.femaleEquip2 = packModel(from, to, def.femaleEquip2, rs3);
		def.femaleEquip3 = packModel(from, to, def.femaleEquip3, rs3);
		def.maleHead1 = packModel(from, to, def.maleHead1, rs3);
		def.maleHead2 = packModel(from, to, def.maleHead2, rs3);
		def.femaleHead1 = packModel(from, to, def.femaleHead1, rs3);
		def.femaleHead2 = packModel(from, to, def.femaleHead2, rs3);
		if (rs3 && def.originalTextureIds != null) {
			for (int i = 0;i < def.originalTextureIds.length;i++) {
				def.originalTextureIds[i] = TextureConverter.convert(from, to, def.originalTextureIds[i], true);
			}
			for (int i = 0;i < def.modifiedTextureIds.length;i++) {
				def.modifiedTextureIds[i] = TextureConverter.convert(from, to, def.modifiedTextureIds[i], true);
			}
		}
	}
}
