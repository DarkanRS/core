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
package com.rs.cache.loaders.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.SuppressWarnings;

import com.rs.cache.Cache;
import com.rs.cache.IndexType;
import com.rs.cache.Store;
import com.rs.cache.loaders.ParticleProducerDefinitions;
import com.rs.lib.io.InputStream;
import com.rs.lib.io.OutputStream;
import com.rs.lib.util.RSColor;
import com.rs.lib.util.Utils;

public class RSModel {

	public int id;
	public byte priority;
	public int version = 12;
	public int vertexCount = 0;
	public int faceCount;
	public int texturedFaceCount;
	public int maxVertexUsed = 0;
	public int textureUVCoordCount;
	public float[] textureCoordU;
	public float[] textureCoordV;
	public byte[] uvCoordVertexA;
	public byte[] uvCoordVertexB;
	public byte[] uvCoordVertexC;
	public byte[] facePriorities;
	public byte[] faceAlpha;
	public byte[] faceRenderTypes;
	public byte[] textureRenderTypes;
	public byte[] textureRotationY;
	public byte[] textureUVDirections;
	public int[] particleLifespanZ;
	public int[] textureScaleX;
	public int[] textureScaleY;
	public int[] textureScaleZ;
	public int[] texturePrimaryColor;
	public int[] textureSecondaryColor;
	public int[] faceBones;
	public int[] vertexX;
	public int[] vertexY;
	public int[] vertexZ;
	public int[] vertexBones;
	public int[] vertexUVOffset;
	public short[] faceTextureIndexes;
	public short[] textureTriangleX;
	public short[] textureTriangleY;
	public short[] textureTriangleZ;
	public short[] faceA;
	public short[] faceB;
	public short[] faceC;
	public short[] faceTextures;
	public short[] faceColors;
	public EmitterConfig[] emitterConfigs;
	public MagnetConfig[] magnetConfigs;
	public BillBoardConfig[] billBoardConfigs;
	public short[] aShortArray1980;
	public short[] aShortArray1981;

	public static void main(String[] args) throws IOException {
		Cache.init("../cache/");
		//RSMesh mesh = getMesh(NPCDefinitions.getNPCDefinitions(0).headModels[0]);
//		AnimationDefinitions defs = AnimationDefinitions.getDefs(7280);
//		RSModel mesh = getMesh(29132);
//		System.out.println(mesh.vertexBones.length);
//		System.out.println(AnimationFrameSet.getFrameSet(defs.getFrameSets()[0].id).getFrames()[0]);
		List<RSModel> meshes = new ArrayList<>();
		for (int i = 0;i < Cache.STORE.getIndex(IndexType.MODELS).getLastArchiveId();i++) {
			RSModel model = getMesh(i);
			if (model.getAvgColor() == 0)
				continue;
			if (model != null)
				meshes.add(model);
		}
		int baseCol = RSColor.RGB_to_HSL(255,255,0);
		meshes.sort((m1, m2) -> {
			//if ((m1.vertexCount + m1.faceCount) == (m2.vertexCount + m2.faceCount))
			//	return Math.abs(m1.getAvgColor()-baseCol) - Math.abs(m2.getAvgColor()-baseCol);
			return (m1.vertexCount + m1.faceCount + Math.abs(m1.getAvgColor()-baseCol)) / 2 - (m2.vertexCount + m2.faceCount + Math.abs(m2.getAvgColor()-baseCol)) / 2;
		});
		int count = 0;
		for (RSModel model : meshes) {
			if (count++ > 100)
				break;
			System.out.println(model.id + " - " + (model.vertexCount + model.faceCount) + " - " + Math.abs(model.getAvgColor()-baseCol));
		}
	}
	
	public short getAvgColor() {
		int total = 0;
		int count = 0;
		for (int i = 0;i < faceCount;i++) {
			if (faceColors[i] == Short.MAX_VALUE)
				continue;
			total += faceColors[i];
			count++;
		}
		if (count == 0)
			return 0;
		return (short) (total / count);
	}

	RSModel(byte[] data, boolean rs3) {
		version = 12;
		priority = (byte) 0;
		vertexCount = 0;
		maxVertexUsed = 0;
		faceCount = 0;
		priority = (byte) 0;
		texturedFaceCount = 0;

		if (rs3) {
			decodeRS3(data);
		} else {
			if (data[data.length - 1] == -1 && data[data.length - 2] == -1) {
				this.decode663(data);
			} else {
				this.decode317(data);
			}
		}
	}

	public RSModel(byte[] data) {
		this(data, false);
	}

	final int getFaces(RSModel rsmesh_1, int i_2, short s_3) {
		int i_4 = rsmesh_1.vertexX[i_2];
		int i_5 = rsmesh_1.vertexY[i_2];
		int i_6 = rsmesh_1.vertexZ[i_2];

		for (int i_7 = 0; i_7 < this.vertexCount; i_7++) {
			if (i_4 == this.vertexX[i_7] && i_5 == this.vertexY[i_7] && i_6 == this.vertexZ[i_7]) {
				this.aShortArray1980[i_7] |= s_3;
				return i_7;
			}
		}

		this.vertexX[this.vertexCount] = i_4;
		this.vertexY[this.vertexCount] = i_5;
		this.vertexZ[this.vertexCount] = i_6;
		this.aShortArray1980[this.vertexCount] = s_3;
		this.vertexBones[this.vertexCount] = rsmesh_1.vertexBones != null ? rsmesh_1.vertexBones[i_2] : -1;
		return this.vertexCount++;
	}

	void decodeRS3(byte[] is) {
		faceCount = 0;
		priority = (byte) 0;
		texturedFaceCount = 0;

		ByteBuffer[] buffers = new ByteBuffer[7];
		for (int i = 0; i < buffers.length; i++)
			buffers[i] = ByteBuffer.wrap(is);

		int modelType = buffers[0].get() & 0xFF;
		if (modelType != 1)
			System.out.println("Invalid model identifier: " + modelType);
		else {
			buffers[0].get();
			version = buffers[0].get() & 0xFF;
			buffers[0].position(is.length - 26);
			vertexCount = buffers[0].getShort() & 0xFFFF;
			faceCount = buffers[0].getShort() & 0xFFFF;
			texturedFaceCount = buffers[0].getShort() & 0xFFFF;
			int flags = buffers[0].get() & 0xFF;
			boolean hasFaceRenderTypes = (flags & 0x1) == 1;
			boolean hasParticleEffects = (flags & 0x2) == 2;
			boolean hasBillboards = (flags & 0x4) == 4;
			boolean hasExternalVertexBones = (flags & 0x10) == 16;
			boolean hasExternalFaceBones = (flags & 0x20) == 32;
			boolean hasExternalPriorities = (flags & 0x40) == 64;
			boolean hasTextureUV = (flags & 0x80) == 128;
			int modelPriority = buffers[0].get() & 0xFF;
			int hasFaceAlpha = buffers[0].get() & 0xFF;
			int hasFaceBones = buffers[0].get() & 0xFF;
			int hasFaceTextures = buffers[0].get() & 0xFF;
			int hasVertexBones = buffers[0].get() & 0xFF;
			int vertexXDataSize = buffers[0].getShort() & 0xFFFF;
			int vertexYDataSize = buffers[0].getShort() & 0xFFFF;
			int vertexZDataSize = buffers[0].getShort() & 0xFFFF;
			int faceDataSize = buffers[0].getShort() & 0xFFFF;
			int faceTextureIndexSize = buffers[0].getShort() & 0xFFFF;
			int vertexBoneDataSize = buffers[0].getShort() & 0xFFFF;
			int faceBoneDataSize = buffers[0].getShort() & 0xFFFF;
			if (!hasExternalVertexBones) {
				if (hasVertexBones == 1)
					vertexBoneDataSize = vertexCount;
				else
					vertexBoneDataSize = 0;
			}
			if (!hasExternalFaceBones) {
				if (hasFaceBones == 1)
					faceBoneDataSize = faceCount;
				else
					faceBoneDataSize = 0;
			}
			int simpleTexFaceCnt = 0;
			int complexTexFaceCnt = 0;
			int cubeTexFaceCnt = 0;
			if (texturedFaceCount > 0) {
				textureRenderTypes = new byte[texturedFaceCount];
				buffers[0].position(3);
				for (int tri = 0; tri < texturedFaceCount; tri++) {
					byte renderType = textureRenderTypes[tri] = buffers[0].get();
					if (renderType == 0)
						simpleTexFaceCnt++;
					if (renderType >= 1 && renderType <= 3)
						complexTexFaceCnt++;
					if (renderType == 2)
						cubeTexFaceCnt++;
				}
			}
			int accumulator = 3 + texturedFaceCount;
			int vertexReadFlagOffset = accumulator;
			accumulator += vertexCount;
			int face_render_info_offset = accumulator;
			if (hasFaceRenderTypes)
				accumulator += faceCount;
			int face_read_flag_offset = accumulator;
			accumulator += faceCount;
			int face_priority_offset = accumulator;
			if (modelPriority == 255)
				accumulator += faceCount;
			int face_bone_offset = accumulator;
			accumulator += faceBoneDataSize;
			int vertex_bone_offset = accumulator;
			accumulator += vertexBoneDataSize;
			int face_alpha_offset = accumulator;
			if (hasFaceAlpha == 1)
				accumulator += faceCount;
			int face_data_offset = accumulator;
			accumulator += faceDataSize;
			int face_texture_offset = accumulator;
			if (hasFaceTextures == 1)
				accumulator += faceCount * 2;
			int face_texture_index_offset = accumulator;
			accumulator += faceTextureIndexSize;
			int face_colour_offset = accumulator;
			accumulator += faceCount * 2;
			int vertex_x_data_offset = accumulator;
			accumulator += vertexXDataSize;
			int vertex_y_data_offset = accumulator;
			accumulator += vertexYDataSize;
			int vertex_z_data_offset = accumulator;
			accumulator += vertexZDataSize;
			int simple_tex_pmn_offset = accumulator;
			accumulator += simpleTexFaceCnt * 6;
			int complex_tex_pmn_offset = accumulator;
			accumulator += complexTexFaceCnt * 6;
			int tex_chunk_size = 6;
			if (version == 14)
				tex_chunk_size = 7;
			else if (version >= 15)
				tex_chunk_size = 9;
			int tex_scale_offset = accumulator;
			accumulator += complexTexFaceCnt * tex_chunk_size;
			int tex_rot_offset = accumulator;
			accumulator += complexTexFaceCnt;
			int tex_dir_offset = accumulator;
			accumulator += complexTexFaceCnt;
			int particleZLifespanAndTextureColorOffset = accumulator;
			accumulator += complexTexFaceCnt + cubeTexFaceCnt * 2;
			int extras_offset = accumulator;
			int face_uv_index_offset = is.length;
			int vertex_uv_offset = is.length;
			int tex_coord_u_offset = is.length;
			int tex_coord_v_offset = is.length;
			if (hasTextureUV) {
				ByteBuffer uvBuffer = ByteBuffer.wrap(is);
				uvBuffer.position(is.length - 26);
				uvBuffer.position(uvBuffer.position() - is[uvBuffer.position() - 1]);
				textureUVCoordCount = uvBuffer.getShort() & 0xFFFF;
				int extras_data_size = uvBuffer.getShort() & 0xFFFF;
				int uv_index_data_size = uvBuffer.getShort() & 0xFFFF;
				face_uv_index_offset = extras_offset + extras_data_size;
				vertex_uv_offset = face_uv_index_offset + uv_index_data_size;
				tex_coord_u_offset = vertex_uv_offset + vertexCount;
				tex_coord_v_offset = tex_coord_u_offset + textureUVCoordCount * 2;
			}
			vertexX = new int[vertexCount];
			vertexY = new int[vertexCount];
			vertexZ = new int[vertexCount];
			faceA = new short[faceCount];
			faceB = new short[faceCount];
			faceC = new short[faceCount];
			if (hasVertexBones == 1)
				vertexBones = new int[vertexCount];
			if (hasFaceRenderTypes)
				faceRenderTypes = new byte[faceCount];
			if (modelPriority == 255)
				facePriorities = new byte[faceCount];
			else
				priority = (byte) modelPriority;
			if (hasFaceAlpha == 1)
				faceAlpha = new byte[faceCount];
			if (hasFaceBones == 1)
				faceBones = new int[faceCount];
			if (hasFaceTextures == 1)
				faceTextures = new short[faceCount];
			if (hasFaceTextures == 1 && (texturedFaceCount > 0 || textureUVCoordCount > 0))
				faceTextureIndexes = new short[faceCount];
			faceColors = new short[faceCount];
			if (texturedFaceCount > 0) {
				textureTriangleX = new short[texturedFaceCount];
				textureTriangleY = new short[texturedFaceCount];
				textureTriangleZ = new short[texturedFaceCount];
				if (complexTexFaceCnt > 0) {
					textureScaleX = new int[complexTexFaceCnt];
					textureScaleY = new int[complexTexFaceCnt];
					textureScaleZ = new int[complexTexFaceCnt];
					textureRotationY = new byte[complexTexFaceCnt];
					textureUVDirections = new byte[complexTexFaceCnt];
					particleLifespanZ = new int[complexTexFaceCnt];
				}
				if (cubeTexFaceCnt > 0) {
					texturePrimaryColor = new int[cubeTexFaceCnt];
					textureSecondaryColor = new int[cubeTexFaceCnt];
				}
			}
			buffers[0].position(vertexReadFlagOffset);
			buffers[1].position(vertex_x_data_offset);
			buffers[2].position(vertex_y_data_offset);
			buffers[3].position(vertex_z_data_offset);
			buffers[4].position(vertex_bone_offset);
			int prevX = 0;
			int prevY = 0;
			int prevZ = 0;
			for (int point = 0; point < vertexCount; point++) {
				int component_flags = buffers[0].get() & 0xFF;
				int dx = 0;
				if ((component_flags & 0x1) != 0)
					dx = ByteBufferUtils.getSmart1(buffers[1]);
				int dy = 0;
				if ((component_flags & 0x2) != 0)
					dy = ByteBufferUtils.getSmart1(buffers[2]);
				int dz = 0;
				if ((component_flags & 0x4) != 0)
					dz = ByteBufferUtils.getSmart1(buffers[3]);
				vertexX[point] = prevX + dx;
				vertexY[point] = prevY + dy;
				vertexZ[point] = prevZ + dz;
				prevX = vertexX[point];
				prevY = vertexY[point];
				prevZ = vertexZ[point];
				if (hasVertexBones == 1) {
					if (hasExternalVertexBones)
						vertexBones[point] = ByteBufferUtils.getSmart2(buffers[4]);
					else {
						vertexBones[point] = buffers[4].get() & 0xFF;
						if (vertexBones[point] == 255)
							vertexBones[point] = -1;
					}
				}
			}
			if (textureUVCoordCount > 0) {
				buffers[0].position(vertex_uv_offset);
				buffers[1].position(tex_coord_u_offset);
				buffers[2].position(tex_coord_v_offset);
				vertexUVOffset = new int[vertexCount];
				int coord = 0;
				int size = 0;
				for (; coord < vertexCount; coord++) {
					vertexUVOffset[coord] = size;
					size += buffers[0].get() & 0xFF;
				}
				uvCoordVertexA = new byte[faceCount];
				uvCoordVertexB = new byte[faceCount];
				uvCoordVertexC = new byte[faceCount];
				textureCoordU = new float[textureUVCoordCount];
				textureCoordV = new float[textureUVCoordCount];
				for (coord = 0; coord < textureUVCoordCount; coord++) {
					textureCoordU[coord] = (buffers[1].getShort() / 4096.0F);
					textureCoordV[coord] = (buffers[2].getShort() / 4096.0F);
				}
			}
			buffers[0].position(face_colour_offset);
			buffers[1].position(face_render_info_offset);
			buffers[2].position(face_priority_offset);
			buffers[3].position(face_alpha_offset);
			buffers[4].position(face_bone_offset);
			buffers[5].position(face_texture_offset);
			buffers[6].position(face_texture_index_offset);
			for (int tri = 0; tri < faceCount; tri++) {
				faceColors[tri] = (short) (buffers[0].getShort() & 0xFFFF);
				if (hasFaceRenderTypes)
					faceRenderTypes[tri] = buffers[1].get();
				if (modelPriority == 255)
					facePriorities[tri] = buffers[2].get();
				if (hasFaceAlpha == 1)
					faceAlpha[tri] = buffers[3].get();
				if (hasFaceBones == 1) {
					if (hasExternalFaceBones)
						faceBones[tri] = ByteBufferUtils.getSmart2(buffers[4]);
					else {
						faceBones[tri] = buffers[4].get() & 0xFF;
						if (faceBones[tri] == 255)
							faceBones[tri] = -1;
					}
				}
				if (hasFaceTextures == 1)
					faceTextures[tri] = (short) ((buffers[5].getShort() & 0xFFFF) - 1);
				if (faceTextureIndexes != null) {
					if (faceTextures[tri] != -1) {
						if (version >= 16)
							faceTextureIndexes[tri] = (short) (ByteBufferUtils.getSmart(buffers[6]) - 1);
						else
							faceTextureIndexes[tri] = (short) ((buffers[6].get() & 0xFF) - 1);
					} else
						faceTextureIndexes[tri] = (short) -1;
				}
			}
			maxVertexUsed = -1;
			buffers[0].position(face_data_offset);
			buffers[1].position(face_read_flag_offset);
			buffers[2].position(face_uv_index_offset);
			calculateMaxDepthRS3(buffers[0], buffers[1], buffers[2]);
			buffers[0].position(simple_tex_pmn_offset);
			buffers[1].position(complex_tex_pmn_offset);
			buffers[2].position(tex_scale_offset);
			buffers[3].position(tex_rot_offset);
			buffers[4].position(tex_dir_offset);
			buffers[5].position(particleZLifespanAndTextureColorOffset);
			decodeTexturedTrianglesRS3(buffers[0], buffers[1], buffers[2], buffers[3], buffers[4], buffers[5]);
			buffers[0].position(extras_offset);
			if (hasParticleEffects) {
				int emitterCount = buffers[0].get() & 0xFF;
				if (emitterCount > 0) {
					emitterConfigs = new EmitterConfig[emitterCount];
					for (int idx = 0; idx < emitterCount; idx++) {
						int type = buffers[0].getShort() & 0xFFFF;
						int face = buffers[0].getShort() & 0xFFFF;
						byte pri;
						if (modelPriority == 255)
							pri = facePriorities[face];
						else
							pri = (byte) modelPriority;
						emitterConfigs[idx] = new EmitterConfig(type, face, faceA[face], faceB[face], faceC[face], pri);
					}
				}
				int magnetCount = buffers[0].get() & 0xFF;
				if (magnetCount > 0) {
					magnetConfigs = new MagnetConfig[magnetCount];
					for (int face = 0; face < magnetCount; face++) {
						int skin = buffers[0].getShort() & 0xFFFF;
						int point = buffers[0].getShort() & 0xFFFF;
						magnetConfigs[face] = new MagnetConfig(skin, point);
					}
				}
			}
			if (hasBillboards) {
				int billBoardCount = buffers[0].get() & 0xFF;
				if (billBoardCount > 0) {
					billBoardConfigs = new BillBoardConfig[billBoardCount];
					for (int vertex = 0; vertex < billBoardCount; vertex++) {
						int type = buffers[0].getShort() & 0xFFFF;
						int face = buffers[0].getShort() & 0xFFFF;
						int priority;
						if (hasExternalPriorities)
							priority = ByteBufferUtils.getSmart2(buffers[0]);
						else {
							priority = buffers[0].get() & 0xFF;
							if (priority == 255)
								priority = -1;
						}
						byte magnitude = buffers[0].get();
						billBoardConfigs[vertex] = new BillBoardConfig(type, face, priority, magnitude);
					}
				}
			}
		}
	}

	void decode663(byte[] data) {
		InputStream first = new InputStream(data);
		InputStream second = new InputStream(data);
		InputStream third = new InputStream(data);
		InputStream fourth = new InputStream(data);
		InputStream fifth = new InputStream(data);
		InputStream sixth = new InputStream(data);
		InputStream seventh = new InputStream(data);
		first.offset = data.length - 23;
		this.vertexCount = first.readUnsignedShort();
		this.faceCount = first.readUnsignedShort();
		this.texturedFaceCount = first.readUnsignedByte();
		int footerFlags = first.readUnsignedByte();
		boolean hasFaceTypes = (footerFlags & 0x1) == 1;
		boolean hasParticleEffects = (footerFlags & 0x2) == 2;
		boolean hasBillboards = (footerFlags & 0x4) == 4;
		boolean hasVersion = (footerFlags & 0x8) == 8;
		if (hasVersion) {
			first.offset -= 7;
			this.version = first.readUnsignedByte();
			first.offset += 6;
		}

		int modelPriority = first.readUnsignedByte();
		int hasFaceAlpha = first.readUnsignedByte();
		int hasFaceSkins = first.readUnsignedByte();
		int hasFaceTextures = first.readUnsignedByte();
		int hasVertexSkins = first.readUnsignedByte();
		int modelVerticesX = first.readUnsignedShort();
		int modelVerticesY = first.readUnsignedShort();
		int modelVerticesZ = first.readUnsignedShort();
		int faceIndices = first.readUnsignedShort();
		int textureIndices = first.readUnsignedShort();
		int simpleTextureFaceCount = 0;
		int complexTextureFaceCount = 0;
		int cubeTextureFaceCount = 0;
		if (this.texturedFaceCount > 0) {
			this.textureRenderTypes = new byte[this.texturedFaceCount];
			first.offset = 0;

			for (int i = 0; i < this.texturedFaceCount; i++) {
				byte type = this.textureRenderTypes[i] = (byte) first.readByte();
				if (type == 0) {
					++simpleTextureFaceCount;
				}

				if (type >= 1 && type <= 3) {
					++complexTextureFaceCount;
				}

				if (type == 2) {
					++cubeTextureFaceCount;
				}
			}
		}

		int offset = this.texturedFaceCount;
		int vertexFlagsOffset = offset;
		//System.out.println("1: " + vertexFlagsOffset);
		offset += this.vertexCount;
		int faceTypesOffset = offset;
		//System.out.println("2: " + (faceTypesOffset - vertexFlagsOffset));
		if (hasFaceTypes) {
			offset += this.faceCount;
		}

		int facesCompressTypeOffset = offset;
		//System.out.println("3: " + (facesCompressTypeOffset - faceTypesOffset));
		offset += this.faceCount;
		int facePrioritiesOffset = offset;
		//System.out.println("4: " + (facePrioritiesOffset - facesCompressTypeOffset));
		if (modelPriority == 255) {
			offset += this.faceCount;
		}

		int faceSkinsOffset = offset;
		//System.out.println("5: " + (faceSkinsOffset - facePrioritiesOffset));
		if (hasFaceSkins == 1) {
			offset += this.faceCount;
		}

		int vertexSkinsOffset = offset;
		//System.out.println("6: " + (vertexSkinsOffset - faceSkinsOffset));
		if (hasVertexSkins == 1) {
			offset += this.vertexCount;
		}

		int faceAlphasOffset = offset;
		//System.out.println("7: " + (faceAlphasOffset - vertexSkinsOffset));
		if (hasFaceAlpha == 1) {
			offset += this.faceCount;
		}

		int faceIndicesOffset = offset;
		//System.out.println("8: " + (faceIndicesOffset - faceAlphasOffset));
		offset += faceIndices;
		int faceMaterialsOffset = offset;
		//System.out.println("9: " + (faceMaterialsOffset - faceIndicesOffset));
		if (hasFaceTextures == 1) {
			offset += this.faceCount * 2;
		}

		int faceTextureIndicesOffset = offset;
		//System.out.println("10: " + (faceTextureIndicesOffset - faceMaterialsOffset));
		offset += textureIndices;
		int faceColorsOffset = offset;
		//System.out.println("11: " + (faceColorsOffset - faceTextureIndicesOffset));
		offset += this.faceCount * 2;
		int vertexXOffsetOffset = offset;
		//System.out.println("12: " + (vertexXOffsetOffset - faceColorsOffset));
		offset += modelVerticesX;
		int vertexYOffsetOffset = offset;
		//System.out.println("13: " + (vertexYOffsetOffset - vertexXOffsetOffset));
		offset += modelVerticesY;
		int vertexZOffsetOffset = offset;
		//System.out.println("14: " + (vertexZOffsetOffset - vertexYOffsetOffset));
		offset += modelVerticesZ;
		int simpleTexturesOffset = offset;
		//System.out.println("15: " + (simpleTexturesOffset - vertexZOffsetOffset));
		offset += simpleTextureFaceCount * 6;
		int complexTexturesOffset = offset;
		//System.out.println("16: " + (complexTexturesOffset - simpleTexturesOffset));
		offset += complexTextureFaceCount * 6;
		byte textureBytes = 6;
		if (this.version == 14) {
			textureBytes = 7;
		} else if (this.version >= 15) {
			textureBytes = 9;
		}

		int texturesScaleOffset = offset;
		//System.out.println("17: " + (texturesScaleOffset - complexTexturesOffset));
		offset += complexTextureFaceCount * textureBytes;
		int texturesRotationOffset = offset;
		//System.out.println("18: " + (texturesRotationOffset - texturesScaleOffset));
		offset += complexTextureFaceCount;
		int texturesDirectionOffset = offset;
		//System.out.println("19: " + (texturesDirectionOffset - texturesRotationOffset));
		offset += complexTextureFaceCount;
		int texturesTranslationOffset = offset;
		//System.out.println("20: " + (texturesTranslationOffset - texturesDirectionOffset));
		offset += complexTextureFaceCount + cubeTextureFaceCount * 2;
		this.vertexX = new int[this.vertexCount];
		this.vertexY = new int[this.vertexCount];
		this.vertexZ = new int[this.vertexCount];
		this.faceA = new short[this.faceCount];
		this.faceB = new short[this.faceCount];
		this.faceC = new short[this.faceCount];
		if (hasVertexSkins == 1) {
			this.vertexBones = new int[this.vertexCount];
		}

		if (hasFaceTypes) {
			this.faceRenderTypes = new byte[this.faceCount];
		}

		if (modelPriority == 255) {
			this.facePriorities = new byte[this.faceCount];
		} else {
			this.priority = (byte) modelPriority;
		}

		if (hasFaceAlpha == 1) {
			this.faceAlpha = new byte[this.faceCount];
		}

		if (hasFaceSkins == 1) {
			this.faceBones = new int[this.faceCount];
		}

		if (hasFaceTextures == 1) {
			this.faceTextures = new short[this.faceCount];
		}

		if (hasFaceTextures == 1 && this.texturedFaceCount > 0) {
			this.faceTextureIndexes = new short[this.faceCount];
		}

		this.faceColors = new short[this.faceCount];
		if (this.texturedFaceCount > 0) {
			this.textureTriangleX = new short[this.texturedFaceCount];
			this.textureTriangleY = new short[this.texturedFaceCount];
			this.textureTriangleZ = new short[this.texturedFaceCount];
			if (complexTextureFaceCount > 0) {
				this.textureScaleX = new int[complexTextureFaceCount];
				this.textureScaleY = new int[complexTextureFaceCount];
				this.textureScaleZ = new int[complexTextureFaceCount];
				this.textureRotationY = new byte[complexTextureFaceCount];
				this.textureUVDirections = new byte[complexTextureFaceCount];
				this.particleLifespanZ = new int[complexTextureFaceCount];
			}

			if (cubeTextureFaceCount > 0) {
				this.texturePrimaryColor = new int[cubeTextureFaceCount];
				this.textureSecondaryColor = new int[cubeTextureFaceCount];
			}
		}

		first.offset = vertexFlagsOffset;
		second.offset = vertexXOffsetOffset;
		third.offset = vertexYOffsetOffset;
		fourth.offset = vertexZOffsetOffset;
		fifth.offset = vertexSkinsOffset;
		int baseX = 0;
		int baseY = 0;
		int baseZ = 0;

		for (int vertex = 0; vertex < this.vertexCount; vertex++) {
			int vertexFlags = first.readUnsignedByte();
			int xOffset = 0;
			if ((vertexFlags & 0x1) != 0) {
				xOffset = second.readSignedSmart();
			}

			int yOffset = 0;
			if ((vertexFlags & 0x2) != 0) {
				yOffset = third.readSignedSmart();
			}

			int zOffset = 0;
			if ((vertexFlags & 0x4) != 0) {
				zOffset = fourth.readSignedSmart();
			}

			this.vertexX[vertex] = baseX + xOffset;
			this.vertexY[vertex] = baseY + yOffset;
			this.vertexZ[vertex] = baseZ + zOffset;
			baseX = this.vertexX[vertex];
			baseY = this.vertexY[vertex];
			baseZ = this.vertexZ[vertex];
			if (hasVertexSkins == 1) {
				this.vertexBones[vertex] = fifth.readUnsignedByte();
				if (vertexBones[vertex] == 255)
					vertexBones[vertex] = -1;
			}
		}

		first.offset = faceColorsOffset;
		second.offset = faceTypesOffset;
		third.offset = facePrioritiesOffset;
		fourth.offset = faceAlphasOffset;
		fifth.offset = faceSkinsOffset;
		sixth.offset = faceMaterialsOffset;
		seventh.offset = faceTextureIndicesOffset;

		for (int face = 0; face < this.faceCount; face++) {
			this.faceColors[face] = (short) first.readUnsignedShort();
			if (hasFaceTypes) {
				this.faceRenderTypes[face] = (byte) second.readByte();
			}

			if (modelPriority == 255) {
				this.facePriorities[face] = (byte) third.readByte();
			}

			if (hasFaceAlpha == 1) {
				this.faceAlpha[face] = (byte) fourth.readByte();
			}

			if (hasFaceSkins == 1) {
				this.faceBones[face] = fifth.readUnsignedByte();
			}

			if (hasFaceTextures == 1) {
				this.faceTextures[face] = (short) (sixth.readUnsignedShort() - 1);
			}

			if (this.faceTextureIndexes != null) {
				if (this.faceTextures[face] != -1) {
					this.faceTextureIndexes[face] = (byte) (seventh.readUnsignedByte() - 1);
				} else {
					this.faceTextureIndexes[face] = -1;
				}
			}
		}

		this.maxVertexUsed = -1;
		first.offset = faceIndicesOffset;
		second.offset = facesCompressTypeOffset;
		this.calculateMaxDepth(first, second);
		first.offset = simpleTexturesOffset;
		second.offset = complexTexturesOffset;
		third.offset = texturesScaleOffset;
		fourth.offset = texturesRotationOffset;
		fifth.offset = texturesDirectionOffset;
		sixth.offset = texturesTranslationOffset;
		this.decodeTexturedTriangles(first, second, third, fourth, fifth, sixth);
		first.offset = offset;
		if (hasParticleEffects) {
			int particleIdx = first.readUnsignedByte();
			if (particleIdx > 0) {
				this.emitterConfigs = new EmitterConfig[particleIdx];

				for (int i = 0; i < particleIdx; i++) {
					int particleId = first.readUnsignedShort();
					int idx = first.readUnsignedShort();
					byte pri;
					if (modelPriority == 255) {
						pri = this.facePriorities[idx];
					} else {
						pri = (byte) modelPriority;
					}

					this.emitterConfigs[i] = new EmitterConfig(particleId, idx, this.faceA[idx], this.faceB[idx], this.faceC[idx], pri);
				}
			}

			int numEffectors = first.readUnsignedByte();
			if (numEffectors > 0) {
				this.magnetConfigs = new MagnetConfig[numEffectors];

				for (int i = 0; i < numEffectors; i++) {
					int effector = first.readUnsignedShort();
					int vertex = first.readUnsignedShort();
					this.magnetConfigs[i] = new MagnetConfig(effector, vertex);
				}
			}
		}

		if (hasBillboards) {
			int billboardIdx = first.readUnsignedByte();
			if (billboardIdx > 0) {
				this.billBoardConfigs = new BillBoardConfig[billboardIdx];

				for (int i = 0; i < billboardIdx; i++) {
					int id = first.readUnsignedShort();
					int face = first.readUnsignedShort();
					int skin = first.readUnsignedByte();
					byte dist = (byte) first.readByte();
					this.billBoardConfigs[i] = new BillBoardConfig(id, face, skin, dist);
				}
			}
		}

	}

	public byte[] encode() {
		/* create the master buffer */
		OutputStream master = new OutputStream();
		/* create the temporary buffers */
		OutputStream face_mappings_buffer = new OutputStream();
		OutputStream vertex_flags_buffer = new OutputStream();
		OutputStream face_types_buffer = new OutputStream();
		OutputStream face_index_types_buffer = new OutputStream();
		OutputStream face_priorities_buffer = new OutputStream();
		OutputStream face_skins_buffer = new OutputStream();
		OutputStream vertex_skins_buffer = new OutputStream();
		OutputStream face_alphas_buffer = new OutputStream();
		OutputStream face_indices_buffer = new OutputStream();
		OutputStream face_materials_buffer = new OutputStream();
		OutputStream face_textures_buffer = new OutputStream();
		OutputStream face_colors_buffer = new OutputStream();
		OutputStream vertex_x_buffer = new OutputStream();
		OutputStream vertex_y_buffer = new OutputStream();
		OutputStream vertex_z_buffer = new OutputStream();
		OutputStream simple_textures_buffer = new OutputStream();
		OutputStream complex_textures_buffer = new OutputStream();
		OutputStream texture_scale_buffer = new OutputStream();
		OutputStream texture_rotation_buffer = new OutputStream();
		OutputStream texture_direction_buffer = new OutputStream();
		OutputStream texture_translation_buffer = new OutputStream();
		OutputStream particle_effects_buffer = new OutputStream();
		OutputStream footer_buffer = new OutputStream();
		OutputStream[] buffers = new OutputStream[] { 
				face_mappings_buffer, 		//1
				vertex_flags_buffer, 		//2
				face_types_buffer, 			//3
				face_index_types_buffer, 	//4
				face_priorities_buffer, 	//5
				face_skins_buffer, 			//6
				vertex_skins_buffer, 		//7
				face_alphas_buffer, 		//8
				face_indices_buffer, 		//9
				face_materials_buffer, 		//10
				face_textures_buffer, 		//11
				face_colors_buffer, 		//12
				vertex_x_buffer, 			//13
				vertex_y_buffer, 			//14
				vertex_z_buffer, 			//15
				simple_textures_buffer, 	//16 
				complex_textures_buffer, 	//17
				texture_scale_buffer, 		//18
				texture_rotation_buffer, 	//19
				texture_direction_buffer, 	//20
				texture_translation_buffer, //21
				particle_effects_buffer, 	//22
				footer_buffer 				//23
			};
		/* serialize the face mapping types */
		if (texturedFaceCount > 0) {
			for (int face = 0; face < texturedFaceCount; face++) {
				face_mappings_buffer.writeByte(textureRenderTypes[face]);
			}
		}

		/* create the vertices variables */
		boolean hasVertexSkins = vertexBones != null;
		/* serialize the vertices */
		int baseX = 0, baseY = 0, baseZ = 0;
		for (int vertex = 0; vertex < vertexCount; vertex++) {
			int x = vertexX[vertex];
			int y = vertexY[vertex];
			int z = vertexZ[vertex];
			int xoff = x - baseX;
			int yoff = y - baseY;
			int zoff = z - baseZ;
			int flag = 0;
			if (xoff != 0) {
				vertex_x_buffer.writeUnsignedSmart(xoff);
				flag |= 0x1;
			}
			if (yoff != 0) {
				vertex_y_buffer.writeUnsignedSmart(yoff);
				flag |= 0x2;
			}
			if (zoff != 0) {
				vertex_z_buffer.writeUnsignedSmart(zoff);
				flag |= 0x4;
			}
			vertex_flags_buffer.writeByte(flag);
			vertexX[vertex] = baseX + xoff;
			vertexY[vertex] = baseY + yoff;
			vertexZ[vertex] = baseZ + zoff;
			baseX = vertexX[vertex];
			baseY = vertexY[vertex];
			baseZ = vertexZ[vertex];
			if (hasVertexSkins) {
				vertex_skins_buffer.writeByte(vertexBones[vertex] == -1 ? 255 : vertexBones[vertex]);
			}
		}
		/* create the faces variables */
		boolean hasFaceTypes = faceRenderTypes != null;
		boolean hasFacePriorities = facePriorities != null;
		boolean hasFaceAlpha = faceAlpha != null;
		boolean hasFaceSkins = faceBones != null;
		boolean hasFaceTextures = faceTextures != null;
		/* serialize the faces */
		for (int face = 0; face < faceCount; face++) {
			face_colors_buffer.writeShort(faceColors[face]);
			if (hasFaceTypes) {
				face_types_buffer.writeByte(faceRenderTypes[face]);
			}
			if (hasFacePriorities) {
				face_priorities_buffer.writeByte(facePriorities[face]);
			}
			if (hasFaceAlpha) {
				face_alphas_buffer.writeByte(faceAlpha[face]);
			}
			if (hasFaceSkins) {
				face_skins_buffer.writeByte(faceBones[face]);
			}
			if (hasFaceTextures) {
				face_materials_buffer.writeShort(faceTextures[face] + 1);
			}
			if (faceTextures != null) {
				if (faceTextures[face] != -1) {
					face_textures_buffer.writeByte(faceTextureIndexes[face] + 1);
				}
			}
		}
		/* serialize the face indices */
		encodeIndices(face_indices_buffer, face_index_types_buffer);
		/* serialize the texture mapping */
		encodeMapping(simple_textures_buffer, complex_textures_buffer, texture_scale_buffer, texture_rotation_buffer, texture_direction_buffer, texture_translation_buffer);
		/* create the particle effects variables */
		boolean hasParticleEffects = emitterConfigs != null || magnetConfigs != null;
		/* serialize the particle effects */
		if (hasParticleEffects) {
			int numEmitters = emitterConfigs != null ? emitterConfigs.length : 0;
			particle_effects_buffer.writeByte(numEmitters);
			if (numEmitters > 0) {
				for (int index = 0; index < numEmitters; index++) {
					EmitterConfig triangle = emitterConfigs[index];
					particle_effects_buffer.writeShort(triangle.type);
					particle_effects_buffer.writeShort(triangle.face);
				}
			}
			int numEffectors = magnetConfigs != null ? magnetConfigs.length : 0;
			particle_effects_buffer.writeByte(numEffectors);
			if (numEffectors > 0) {
				for (int index = 0; index < numEffectors; index++) {
					MagnetConfig vertex = magnetConfigs[index];
					particle_effects_buffer.writeShort(vertex.type);
					particle_effects_buffer.writeShort(vertex.vertex);
				}
			}
		}
		/* create the billboards variables */
		boolean hasBillboards = billBoardConfigs != null;
		/* serialize the billboards */
		if (hasBillboards) {
			particle_effects_buffer.writeByte(billBoardConfigs.length);
			for (int index = 0; index < billBoardConfigs.length; index++) {
				BillBoardConfig billboard = billBoardConfigs[index];
				particle_effects_buffer.writeShort(billboard.type);
				particle_effects_buffer.writeShort(billboard.face);
				particle_effects_buffer.writeByte(billboard.priority);
				particle_effects_buffer.writeByte(billboard.magnitude);
			}
		}
		/* create the footer data */
		boolean hasVersion = version != 12;
		if (hasVersion) {
			footer_buffer.writeByte(version);
		}
		footer_buffer.writeShort(vertexCount);
		footer_buffer.writeShort(faceCount);
		footer_buffer.writeByte(texturedFaceCount);
		int flags = 0;
		if (hasFaceTypes) {
			flags |= 0x1;
		}
		if (hasParticleEffects) {
			flags |= 0x2;
		}
		if (hasBillboards) {
			flags |= 0x4;
		}
		if (hasVersion) {
			flags |= 0x8;
		}
		footer_buffer.writeByte(flags);
		footer_buffer.writeByte(hasFacePriorities ? -1 : priority);
		footer_buffer.writeBoolean(hasFaceAlpha);
		footer_buffer.writeBoolean(hasFaceSkins);
		footer_buffer.writeBoolean(hasFaceTextures);
		footer_buffer.writeBoolean(hasVertexSkins);
		footer_buffer.writeShort(vertex_x_buffer.getOffset());
		footer_buffer.writeShort(vertex_y_buffer.getOffset());
		footer_buffer.writeShort(vertex_z_buffer.getOffset());
		footer_buffer.writeShort(face_indices_buffer.getOffset());
		footer_buffer.writeShort(face_textures_buffer.getOffset());
		for (int i = 0; i < buffers.length; i++) {
			OutputStream buffer = buffers[i];
			//int before = master.getOffset();
			master.writeBytes(buffer.toByteArray());
			//System.out.println("buffer: " + (i+1) + " size: " + (master.getOffset() - before));
		}
		master.writeByte(-1);
		master.writeByte(-1);
		return master.toByteArray();
	}

	private void encodeMapping(OutputStream simple, OutputStream complex, OutputStream scale, OutputStream rotation, OutputStream direction, OutputStream translation) {
		for (int face = 0; face < texturedFaceCount; face++) {
			int type = textureRenderTypes[face] & 0xff;
			if (type == 0) {
				simple.writeShort(textureTriangleX[face]);
				simple.writeShort(textureTriangleY[face]);
				simple.writeShort(textureTriangleZ[face]);
			} else {
				int scaleX = textureScaleX[face];
				int scaleY = textureScaleY[face];
				int scaleZ = textureScaleZ[face];
				if (type == 1) {
					complex.writeShort(textureTriangleX[face]);
					complex.writeShort(textureTriangleY[face]);
					complex.writeShort(textureTriangleZ[face]);
					if (version >= 15 || scaleX > 0xffff || scaleZ > 0xffff) {
						if (version < 15) {
							version = 15;
						}
						scale.write24BitInt(scaleX);
						scale.write24BitInt(scaleY);
						scale.write24BitInt(scaleZ);
					} else {
						scale.writeShort(scaleX);
						if (version < 14 && scaleY > 0xffff) {
							version = 14;
						}
						if (version < 14) {
							scale.writeShort(scaleY);
						} else {
							scale.write24BitInt(scaleY);
						}
						scale.writeShort(scaleZ);
					}
					rotation.writeByte(textureRotationY[face]);
					direction.writeByte(textureUVDirections[face]);
					translation.writeByte(particleLifespanZ[face]);
				} else if (type == 2) {
					complex.writeShort(textureTriangleX[face]);
					complex.writeShort(textureTriangleY[face]);
					complex.writeShort(textureTriangleZ[face]);
					if (version >= 15 || scaleX > 0xffff || scaleZ > 0xffff) {
						if (version < 15) {
							version = 15;
						}
						scale.write24BitInt(scaleX);
						scale.write24BitInt(scaleY);
						scale.write24BitInt(scaleZ);
					} else {
						scale.writeShort(scaleX);
						if (version < 14 && scaleY > 0xffff) {
							version = 14;
						}
						if (version < 14) {
							scale.writeShort(scaleY);
						} else {
							scale.write24BitInt(scaleY);
						}
						scale.writeShort(scaleZ);
					}
					rotation.writeByte(textureRotationY[face]);
					direction.writeByte(textureUVDirections[face]);
					translation.writeByte(particleLifespanZ[face]);
					translation.writeByte(texturePrimaryColor[face]);
					translation.writeByte(textureSecondaryColor[face]);
				} else if (type == 3) {
					complex.writeShort(textureTriangleX[face]);
					complex.writeShort(textureTriangleY[face]);
					complex.writeShort(textureTriangleZ[face]);


					if (version >= 15 || scaleX > 0xffff || scaleZ > 0xffff) {
						if (version < 15) {
							version = 15;
						}
						scale.write24BitInt(scaleX);
						scale.write24BitInt(scaleY);
						scale.write24BitInt(scaleZ);
					} else {
						scale.writeShort(scaleX);
						if (version < 14 && scaleY > 0xffff) {
							version = 14;
						}
						if (version < 14) {
							scale.writeShort(scaleY);
						} else {
							scale.write24BitInt(scaleY);
						}
						scale.writeShort(scaleZ);
					}
					rotation.writeByte(textureRotationY[face]);
					direction.writeByte(textureUVDirections[face]);
					translation.writeByte(particleLifespanZ[face]);
				}
			}
		}
	}

	private void encodeIndices(OutputStream ibuffer, OutputStream tbuffer) {
		short lasta = 0;
		short lastb = 0;
		short lastc = 0;
		int pacc = 0;
		for (int fndex = 0; fndex < faceCount; fndex++) {
			short cura = faceA[fndex];
			short curb = faceB[fndex];
			short curc = faceC[fndex];
			if (cura == lastb && curb == lasta && curc != lastc) {
				tbuffer.writeByte(4);
				ibuffer.writeUnsignedSmart(curc - pacc);
				short back = lasta;
				lasta = lastb;
				lastb = back;
				pacc = lastc = curc;
			} else if (cura == lastc && curb == lastb && curc != lastc) {
				tbuffer.writeByte(3);
				ibuffer.writeUnsignedSmart(curc - pacc);
				lasta = lastc;
				pacc = lastc = curc;
			} else if (cura == lasta && curb == lastc && curc != lastc) {
				tbuffer.writeByte(2);
				ibuffer.writeUnsignedSmart(curc - pacc);
				lastb = lastc;
				pacc = lastc = curc;
			} else {
				tbuffer.writeByte(1);
				ibuffer.writeUnsignedSmart(cura - pacc);
				ibuffer.writeUnsignedSmart(curb - cura);
				ibuffer.writeUnsignedSmart(curc - curb);
				lasta = cura;
				lastb = curb;
				pacc = lastc = curc;
			}


		}
	}

	void calculateMaxDepthRS3(ByteBuffer class475_sub17, ByteBuffer class475_sub17_288_, ByteBuffer class475_sub17_289_) {
		short i = 0;
		short i_290_ = 0;
		short i_291_ = 0;
		int i_292_ = 0;
		for (int i_293_ = 0; i_293_ < faceCount; i_293_++) {
			int i_294_ = class475_sub17_288_.get() & 0xFF;
			int i_295_ = i_294_ & 0x7;
			if (i_295_ == 1) {
				faceA[i_293_] = i = (short) (ByteBufferUtils.getSmart1(class475_sub17) + i_292_);
				i_292_ = i;
				faceB[i_293_] = i_290_ = (short) (ByteBufferUtils.getSmart1(class475_sub17) + i_292_);
				i_292_ = i_290_;
				faceC[i_293_] = i_291_ = (short) (ByteBufferUtils.getSmart1(class475_sub17) + i_292_);
				i_292_ = i_291_;
				if (i > maxVertexUsed)
					maxVertexUsed = i;
				if (i_290_ > maxVertexUsed)
					maxVertexUsed = i_290_;
				if (i_291_ > maxVertexUsed)
					maxVertexUsed = i_291_;
			}
			if (i_295_ == 2) {
				i_290_ = i_291_;
				i_291_ = (short) (ByteBufferUtils.getSmart1(class475_sub17) + i_292_);
				i_292_ = i_291_;
				faceA[i_293_] = i;
				faceB[i_293_] = i_290_;
				faceC[i_293_] = i_291_;
				if (i_291_ > maxVertexUsed)
					maxVertexUsed = i_291_;
			}
			if (i_295_ == 3) {
				i = i_291_;
				i_291_ = (short) (ByteBufferUtils.getSmart1(class475_sub17) + i_292_);
				i_292_ = i_291_;
				faceA[i_293_] = i;
				faceB[i_293_] = i_290_;
				faceC[i_293_] = i_291_;
				if (i_291_ > maxVertexUsed)
					maxVertexUsed = i_291_;
			}
			if (i_295_ == 4) {
				short i_296_ = i;
				i = i_290_;
				i_290_ = i_296_;
				i_291_ = (short) (ByteBufferUtils.getSmart1(class475_sub17) + i_292_);
				i_292_ = i_291_;
				faceA[i_293_] = i;
				faceB[i_293_] = i_290_;
				faceC[i_293_] = i_291_;
				if (i_291_ > maxVertexUsed)
					maxVertexUsed = i_291_;
			}
			if (textureUVCoordCount > 0 && (i_294_ & 0x8) != 0) {
				uvCoordVertexA[i_293_] = (byte) (class475_sub17_289_.get() & 0xFF);
				uvCoordVertexB[i_293_] = (byte) (class475_sub17_289_.get() & 0xFF);
				uvCoordVertexC[i_293_] = (byte) (class475_sub17_289_.get() & 0xFF);
			}
		}
		maxVertexUsed++;
	}

	void calculateMaxDepth(InputStream rsbytebuffer_1, InputStream rsbytebuffer_2) {
		short s_3 = 0;
		short s_4 = 0;
		short s_5 = 0;
		short s_6 = 0;

		for (int i_7 = 0; i_7 < this.faceCount; i_7++) {
			int i_8 = rsbytebuffer_2.readUnsignedByte();
			if (i_8 == 1) {
				s_3 = (short) (rsbytebuffer_1.readSignedSmart() + s_6);
				s_4 = (short) (rsbytebuffer_1.readSignedSmart() + s_3);
				s_5 = (short) (rsbytebuffer_1.readSignedSmart() + s_4);
				s_6 = s_5;
				this.faceA[i_7] = s_3;
				this.faceB[i_7] = s_4;
				this.faceC[i_7] = s_5;
				if (s_3 > this.maxVertexUsed) {
					this.maxVertexUsed = s_3;
				}

				if (s_4 > this.maxVertexUsed) {
					this.maxVertexUsed = s_4;
				}

				if (s_5 > this.maxVertexUsed) {
					this.maxVertexUsed = s_5;
				}
			}

			if (i_8 == 2) {
				s_4 = s_5;
				s_5 = (short) (rsbytebuffer_1.readSignedSmart() + s_6);
				s_6 = s_5;
				this.faceA[i_7] = s_3;
				this.faceB[i_7] = s_4;
				this.faceC[i_7] = s_5;
				if (s_5 > this.maxVertexUsed) {
					this.maxVertexUsed = s_5;
				}
			}

			if (i_8 == 3) {
				s_3 = s_5;
				s_5 = (short) (rsbytebuffer_1.readSignedSmart() + s_6);
				s_6 = s_5;
				this.faceA[i_7] = s_3;
				this.faceB[i_7] = s_4;
				this.faceC[i_7] = s_5;
				if (s_5 > this.maxVertexUsed) {
					this.maxVertexUsed = s_5;
				}
			}

			if (i_8 == 4) {
				short s_9 = s_3;
				s_3 = s_4;
				s_4 = s_9;
				s_5 = (short) (rsbytebuffer_1.readSignedSmart() + s_6);
				s_6 = s_5;
				this.faceA[i_7] = s_3;
				this.faceB[i_7] = s_9;
				this.faceC[i_7] = s_5;
				if (s_5 > this.maxVertexUsed) {
					this.maxVertexUsed = s_5;
				}
			}
		}

		++this.maxVertexUsed;
	}

	void decodeTexturedTrianglesRS3(ByteBuffer class475_sub17, ByteBuffer class475_sub17_142_,
			ByteBuffer class475_sub17_143_, ByteBuffer class475_sub17_144_, ByteBuffer class475_sub17_145_,
			ByteBuffer class475_sub17_146_) {
		for (int i = 0; i < texturedFaceCount; i++) {
			int i_147_ = textureRenderTypes[i] & 0xff;
			if (i_147_ == 0) {
				textureTriangleX[i] = (short) (class475_sub17.getShort() & 0xFFFF);
				textureTriangleY[i] = (short) (class475_sub17.getShort() & 0xFFFF);
				textureTriangleZ[i] = (short) (class475_sub17.getShort() & 0xFFFF);
			}
			if (i_147_ == 1) {
				textureTriangleX[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				textureTriangleY[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				textureTriangleZ[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				if (version < 15) {
					textureScaleX[i] = class475_sub17_143_.getShort() & 0xFFFF;
					if (version < 14)
						textureScaleY[i] = class475_sub17_143_.getShort() & 0xFFFF;
					else
						textureScaleY[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleZ[i] = class475_sub17_143_.getShort() & 0xFFFF;
				} else {
					textureScaleX[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleY[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleZ[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
				}
				textureRotationY[i] = class475_sub17_144_.get();
				textureUVDirections[i] = class475_sub17_145_.get();
				particleLifespanZ[i] = class475_sub17_146_.get();
			}
			if (i_147_ == 2) {
				textureTriangleX[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				textureTriangleY[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				textureTriangleZ[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				if (version < 15) {
					textureScaleX[i] = class475_sub17_143_.getShort() & 0xFFFF;
					if (version < 14)
						textureScaleY[i] = class475_sub17_143_.getShort() & 0xFFFF;
					else
						textureScaleY[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleZ[i] = class475_sub17_143_.getShort() & 0xFFFF;
				} else {
					textureScaleX[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleY[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleZ[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
				}
				textureRotationY[i] = class475_sub17_144_.get();
				textureUVDirections[i] = class475_sub17_145_.get();
				particleLifespanZ[i] = class475_sub17_146_.get();
				texturePrimaryColor[i] = class475_sub17_146_.get();
				textureSecondaryColor[i] = class475_sub17_146_.get();
			}
			if (i_147_ == 3) {
				textureTriangleX[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				textureTriangleY[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				textureTriangleZ[i] = (short) (class475_sub17_142_.getShort() & 0xFFFF);
				if (version < 15) {
					textureScaleX[i] = class475_sub17_143_.getShort() & 0xFFFF;
					if (version < 14)
						textureScaleY[i] = class475_sub17_143_.getShort() & 0xFFFF;
					else
						textureScaleY[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleZ[i] = class475_sub17_143_.getShort() & 0xFFFF;
				} else {
					textureScaleX[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleY[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
					textureScaleZ[i] = ByteBufferUtils.getMedium(class475_sub17_143_);
				}
				textureRotationY[i] = class475_sub17_144_.get();
				textureUVDirections[i] = class475_sub17_145_.get();
				particleLifespanZ[i] = class475_sub17_146_.get();
			}
		}
	}

	void decodeTexturedTriangles(InputStream rsbytebuffer_1, InputStream rsbytebuffer_2, InputStream rsbytebuffer_3, InputStream rsbytebuffer_4, InputStream rsbytebuffer_5, InputStream rsbytebuffer_6) {
		for (int i_7 = 0; i_7 < this.texturedFaceCount; i_7++) {
			int i_8 = this.textureRenderTypes[i_7] & 0xff;
			if (i_8 == 0) {
				this.textureTriangleX[i_7] = (short) rsbytebuffer_1.readUnsignedShort();
				this.textureTriangleY[i_7] = (short) rsbytebuffer_1.readUnsignedShort();
				this.textureTriangleZ[i_7] = (short) rsbytebuffer_1.readUnsignedShort();
			}

			if (i_8 == 1) {
				this.textureTriangleX[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				this.textureTriangleY[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				this.textureTriangleZ[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				if (this.version < 15) {
					this.textureScaleX[i_7] = rsbytebuffer_3.readUnsignedShort();
					if (this.version < 14) {
						this.textureScaleY[i_7] = rsbytebuffer_3.readUnsignedShort();
					} else {
						this.textureScaleY[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					}

					this.textureScaleZ[i_7] = rsbytebuffer_3.readUnsignedShort();
				} else {
					this.textureScaleX[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					this.textureScaleY[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					this.textureScaleZ[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
				}

				this.textureRotationY[i_7] = (byte) rsbytebuffer_4.readByte();
				this.textureUVDirections[i_7] = (byte) rsbytebuffer_5.readByte();
				this.particleLifespanZ[i_7] = rsbytebuffer_6.readByte();
			}

			if (i_8 == 2) {
				this.textureTriangleX[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				this.textureTriangleY[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				this.textureTriangleZ[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				if (this.version < 15) {
					this.textureScaleX[i_7] = rsbytebuffer_3.readUnsignedShort();
					if (this.version < 14) {
						this.textureScaleY[i_7] = rsbytebuffer_3.readUnsignedShort();
					} else {
						this.textureScaleY[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					}

					this.textureScaleZ[i_7] = rsbytebuffer_3.readUnsignedShort();
				} else {
					this.textureScaleX[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					this.textureScaleY[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					this.textureScaleZ[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
				}

				this.textureRotationY[i_7] = (byte) rsbytebuffer_4.readByte();
				this.textureUVDirections[i_7] = (byte) rsbytebuffer_5.readByte();
				this.particleLifespanZ[i_7] = rsbytebuffer_6.readByte();
				this.texturePrimaryColor[i_7] = rsbytebuffer_6.readByte();
				this.textureSecondaryColor[i_7] = rsbytebuffer_6.readByte();
			}

			if (i_8 == 3) {
				this.textureTriangleX[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				this.textureTriangleY[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				this.textureTriangleZ[i_7] = (short) rsbytebuffer_2.readUnsignedShort();
				if (this.version < 15) {
					this.textureScaleX[i_7] = rsbytebuffer_3.readUnsignedShort();
					if (this.version < 14) {
						this.textureScaleY[i_7] = rsbytebuffer_3.readUnsignedShort();
					} else {
						this.textureScaleY[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					}

					this.textureScaleZ[i_7] = rsbytebuffer_3.readUnsignedShort();
				} else {
					this.textureScaleX[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					this.textureScaleY[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
					this.textureScaleZ[i_7] = rsbytebuffer_3.read24BitUnsignedInteger();
				}

				this.textureRotationY[i_7] = (byte) rsbytebuffer_4.readByte();
				this.textureUVDirections[i_7] = (byte) rsbytebuffer_5.readByte();
				this.particleLifespanZ[i_7] = rsbytebuffer_6.readByte();
			}
		}

	}

	public int method2662(int i_1, int i_2, int i_3) {
		for (int i_4 = 0; i_4 < this.vertexCount; i_4++) {
			if (this.vertexX[i_4] == i_1 && i_2 == this.vertexY[i_4] && i_3 == this.vertexZ[i_4]) {
				return i_4;
			}
		}

		this.vertexX[this.vertexCount] = i_1;
		this.vertexY[this.vertexCount] = i_2;
		this.vertexZ[this.vertexCount] = i_3;
		this.maxVertexUsed = this.vertexCount + 1;
		return this.vertexCount++;
	}

	public int method2663(int i_1, int i_2, int i_3, byte b_4, byte b_5, short s_6, byte b_7, short s_8) {
		this.faceA[this.faceCount] = (short) i_1;
		this.faceB[this.faceCount] = (short) i_2;
		this.faceC[this.faceCount] = (short) i_3;
		this.faceRenderTypes[this.faceCount] = b_4;
		this.faceTextureIndexes[this.faceCount] = b_5;
		this.faceColors[this.faceCount] = s_6;
		this.faceAlpha[this.faceCount] = b_7;
		this.faceTextures[this.faceCount] = s_8;
		return this.faceCount++;
	}

	public byte method2664() {
		if (this.texturedFaceCount >= 255) {
			throw new IllegalStateException();
		} else {
			this.textureRenderTypes[this.texturedFaceCount] = 3;
			this.textureTriangleX[this.texturedFaceCount] = (short) 0;
			this.textureTriangleY[this.texturedFaceCount] = (short) 32767;
			this.textureTriangleZ[this.texturedFaceCount] = (short) 0;
			this.textureScaleX[this.texturedFaceCount] = (short) 1024;
			this.textureScaleY[this.texturedFaceCount] = (short) 1024;
			this.textureScaleZ[this.texturedFaceCount] = (short) 1024;
			this.textureRotationY[this.texturedFaceCount] = (byte) 0;
			this.textureUVDirections[this.texturedFaceCount] = (byte) 0;
			this.particleLifespanZ[this.texturedFaceCount] = (byte) 0;
			return (byte) (this.texturedFaceCount++);
		}
	}

	public int[][] method2665(boolean bool_1) {
		int[] ints_2 = new int[256];
		int i_3 = 0;
		int i_4 = bool_1 ? this.vertexCount : this.maxVertexUsed;

		int i_6;
		for (int i_5 = 0; i_5 < i_4; i_5++) {
			i_6 = this.vertexBones[i_5];
			if (i_6 >= 0) {
				++ints_2[i_6];
				if (i_6 > i_3) {
					i_3 = i_6;
				}
			}
		}

		int[][] ints_8 = new int[i_3 + 1][];

		for (i_6 = 0; i_6 <= i_3; i_6++) {
			ints_8[i_6] = new int[ints_2[i_6]];
			ints_2[i_6] = 0;
		}

		for (i_6 = 0; i_6 < i_4; i_6++) {
			int i_7 = this.vertexBones[i_6];
			if (i_7 >= 0) {
				ints_8[i_7][ints_2[i_7]++] = i_6;
			}
		}

		return ints_8;
	}

	public int[][] method2666() {
		int[] ints_1 = new int[256];
		int i_2 = 0;

		int i_4;
		for (int i_3 = 0; i_3 < this.faceCount; i_3++) {
			i_4 = this.faceBones[i_3];
			if (i_4 >= 0) {
				++ints_1[i_4];
				if (i_4 > i_2) {
					i_2 = i_4;
				}
			}
		}

		int[][] ints_6 = new int[i_2 + 1][];

		for (i_4 = 0; i_4 <= i_2; i_4++) {
			ints_6[i_4] = new int[ints_1[i_4]];
			ints_1[i_4] = 0;
		}

		for (i_4 = 0; i_4 < this.faceCount; i_4++) {
			int i_5 = this.faceBones[i_4];
			if (i_5 >= 0) {
				ints_6[i_5][ints_1[i_5]++] = i_4;
			}
		}

		return ints_6;
	}

	public int[][] method2667() {
		int[] ints_1 = new int[256];
		int i_2 = 0;

		int i_4;
		for (int i_3 = 0; i_3 < this.billBoardConfigs.length; i_3++) {
			i_4 = this.billBoardConfigs[i_3].priority;
			if (i_4 >= 0) {
				++ints_1[i_4];
				if (i_4 > i_2) {
					i_2 = i_4;
				}
			}
		}

		int[][] ints_6 = new int[i_2 + 1][];

		for (i_4 = 0; i_4 <= i_2; i_4++) {
			ints_6[i_4] = new int[ints_1[i_4]];
			ints_1[i_4] = 0;
		}

		for (i_4 = 0; i_4 < this.billBoardConfigs.length; i_4++) {
			int i_5 = this.billBoardConfigs[i_4].priority;
			if (i_5 >= 0) {
				ints_6[i_5][ints_1[i_5]++] = i_4;
			}
		}

		return ints_6;
	}

	public void recolor(short s_1, short s_2) {
		for (int i_3 = 0; i_3 < this.faceCount; i_3++) {
			if (this.faceColors[i_3] == s_1) {
				this.faceColors[i_3] = s_2;
			}
		}

	}

	public void retexture(short s_1, short s_2) {
		if (this.faceTextures != null) {
			for (int i_3 = 0; i_3 < this.faceCount; i_3++) {
				if (this.faceTextures[i_3] == s_1) {
					this.faceTextures[i_3] = s_2;
				}
			}
		}

	}

	void decode317(byte[] bytes_1) {
		boolean bool_2 = false;
		boolean bool_3 = false;
		InputStream rsbytebuffer_4 = new InputStream(bytes_1);
		InputStream rsbytebuffer_5 = new InputStream(bytes_1);
		InputStream rsbytebuffer_6 = new InputStream(bytes_1);
		InputStream rsbytebuffer_7 = new InputStream(bytes_1);
		InputStream rsbytebuffer_8 = new InputStream(bytes_1);
		rsbytebuffer_4.offset = bytes_1.length - 18;
		this.vertexCount = rsbytebuffer_4.readUnsignedShort();
		this.faceCount = rsbytebuffer_4.readUnsignedShort();
		this.texturedFaceCount = rsbytebuffer_4.readUnsignedByte();
		int i_9 = rsbytebuffer_4.readUnsignedByte();
		int i_10 = rsbytebuffer_4.readUnsignedByte();
		int i_11 = rsbytebuffer_4.readUnsignedByte();
		int i_12 = rsbytebuffer_4.readUnsignedByte();
		int i_13 = rsbytebuffer_4.readUnsignedByte();
		int i_14 = rsbytebuffer_4.readUnsignedShort();
		int i_15 = rsbytebuffer_4.readUnsignedShort();
		int i_16 = rsbytebuffer_4.readUnsignedShort();
		int i_17 = rsbytebuffer_4.readUnsignedShort();
		byte b_18 = 0;
		int i_42 = b_18 + this.vertexCount;
		int i_20 = i_42;
		i_42 += this.faceCount;
		int i_21 = i_42;
		if (i_10 == 255) {
			i_42 += this.faceCount;
		}

		int i_22 = i_42;
		if (i_12 == 1) {
			i_42 += this.faceCount;
		}

		int i_23 = i_42;
		if (i_9 == 1) {
			i_42 += this.faceCount;
		}

		int i_24 = i_42;
		if (i_13 == 1) {
			i_42 += this.vertexCount;
		}

		int i_25 = i_42;
		if (i_11 == 1) {
			i_42 += this.faceCount;
		}

		int i_26 = i_42;
		i_42 += i_17;
		int i_27 = i_42;
		i_42 += this.faceCount * 2;
		int i_28 = i_42;
		i_42 += this.texturedFaceCount * 6;
		int i_29 = i_42;
		i_42 += i_14;
		int i_30 = i_42;
		i_42 += i_15;


		@SuppressWarnings("unused")
		int i_10000 = i_42 + i_16;

		this.vertexX = new int[this.vertexCount];
		this.vertexY = new int[this.vertexCount];
		this.vertexZ = new int[this.vertexCount];
		this.faceA = new short[this.faceCount];
		this.faceB = new short[this.faceCount];
		this.faceC = new short[this.faceCount];
		if (this.texturedFaceCount > 0) {
			this.textureRenderTypes = new byte[this.texturedFaceCount];
			this.textureTriangleX = new short[this.texturedFaceCount];
			this.textureTriangleY = new short[this.texturedFaceCount];
			this.textureTriangleZ = new short[this.texturedFaceCount];
		}

		if (i_13 == 1) {
			this.vertexBones = new int[this.vertexCount];
		}

		if (i_9 == 1) {
			this.faceRenderTypes = new byte[this.faceCount];
			this.faceTextureIndexes = new short[this.faceCount];
			this.faceTextures = new short[this.faceCount];
		}

		if (i_10 == 255) {
			this.facePriorities = new byte[this.faceCount];
		} else {
			this.priority = (byte) i_10;
		}

		if (i_11 == 1) {
			this.faceAlpha = new byte[this.faceCount];
		}

		if (i_12 == 1) {
			this.faceBones = new int[this.faceCount];
		}

		this.faceColors = new short[this.faceCount];
		rsbytebuffer_4.offset = b_18;
		rsbytebuffer_5.offset = i_29;
		rsbytebuffer_6.offset = i_30;
		rsbytebuffer_7.offset = i_42;
		rsbytebuffer_8.offset = i_24;
		int i_32 = 0;
		int i_33 = 0;
		int i_34 = 0;

		int i_35;
		int i_36;
		int i_39;
		for (i_35 = 0; i_35 < this.vertexCount; i_35++) {
			i_36 = rsbytebuffer_4.readUnsignedByte();
			int i_37 = 0;
			if ((i_36 & 0x1) != 0) {
				i_37 = rsbytebuffer_5.readSignedSmart();
			}

			int i_38 = 0;
			if ((i_36 & 0x2) != 0) {
				i_38 = rsbytebuffer_6.readSignedSmart();
			}

			i_39 = 0;
			if ((i_36 & 0x4) != 0) {
				i_39 = rsbytebuffer_7.readSignedSmart();
			}

			this.vertexX[i_35] = i_32 + i_37;
			this.vertexY[i_35] = i_33 + i_38;
			this.vertexZ[i_35] = i_34 + i_39;
			i_32 = this.vertexX[i_35];
			i_33 = this.vertexY[i_35];
			i_34 = this.vertexZ[i_35];
			if (i_13 == 1) {
				this.vertexBones[i_35] = rsbytebuffer_8.readUnsignedByte();
			}
		}

		rsbytebuffer_4.offset = i_27;
		rsbytebuffer_5.offset = i_23;
		rsbytebuffer_6.offset = i_21;
		rsbytebuffer_7.offset = i_25;
		rsbytebuffer_8.offset = i_22;

		for (i_35 = 0; i_35 < this.faceCount; i_35++) {
			this.faceColors[i_35] = (short) rsbytebuffer_4.readUnsignedShort();
			if (i_9 == 1) {
				i_36 = rsbytebuffer_5.readUnsignedByte();
				if ((i_36 & 0x1) == 1) {
					this.faceRenderTypes[i_35] = 1;
					bool_2 = true;
				} else {
					this.faceRenderTypes[i_35] = 0;
				}

				if ((i_36 & 0x2) == 2) {
					this.faceTextureIndexes[i_35] = (byte) (i_36 >> 2);
					this.faceTextures[i_35] = this.faceColors[i_35];
					this.faceColors[i_35] = 127;
					if (this.faceTextures[i_35] != -1) {
						bool_3 = true;
					}
				} else {
					this.faceTextureIndexes[i_35] = -1;
					this.faceTextures[i_35] = -1;
				}
			}

			if (i_10 == 255) {
				this.facePriorities[i_35] = (byte) rsbytebuffer_6.readByte();
			}

			if (i_11 == 1) {
				this.faceAlpha[i_35] = (byte) rsbytebuffer_7.readByte();
			}

			if (i_12 == 1) {
				this.faceBones[i_35] = rsbytebuffer_8.readUnsignedByte();
			}
		}

		this.maxVertexUsed = -1;
		rsbytebuffer_4.offset = i_26;
		rsbytebuffer_5.offset = i_20;
		short s_43 = 0;
		short s_44 = 0;
		short s_45 = 0;
		short s_46 = 0;

		int i_40;
		for (i_39 = 0; i_39 < this.faceCount; i_39++) {
			i_40 = rsbytebuffer_5.readUnsignedByte();
			if (i_40 == 1) {
				s_43 = (short) (rsbytebuffer_4.readSignedSmart() + s_46);
				s_44 = (short) (rsbytebuffer_4.readSignedSmart() + s_43);
				s_45 = (short) (rsbytebuffer_4.readSignedSmart() + s_44);
				s_46 = s_45;
				this.faceA[i_39] = s_43;
				this.faceB[i_39] = s_44;
				this.faceC[i_39] = s_45;
				if (s_43 > this.maxVertexUsed) {
					this.maxVertexUsed = s_43;
				}

				if (s_44 > this.maxVertexUsed) {
					this.maxVertexUsed = s_44;
				}

				if (s_45 > this.maxVertexUsed) {
					this.maxVertexUsed = s_45;
				}
			}

			if (i_40 == 2) {
				s_44 = s_45;
				s_45 = (short) (rsbytebuffer_4.readSignedSmart() + s_46);
				s_46 = s_45;
				this.faceA[i_39] = s_43;
				this.faceB[i_39] = s_44;
				this.faceC[i_39] = s_45;
				if (s_45 > this.maxVertexUsed) {
					this.maxVertexUsed = s_45;
				}
			}

			if (i_40 == 3) {
				s_43 = s_45;
				s_45 = (short) (rsbytebuffer_4.readSignedSmart() + s_46);
				s_46 = s_45;
				this.faceA[i_39] = s_43;
				this.faceB[i_39] = s_44;
				this.faceC[i_39] = s_45;
				if (s_45 > this.maxVertexUsed) {
					this.maxVertexUsed = s_45;
				}
			}

			if (i_40 == 4) {
				short s_41 = s_43;
				s_43 = s_44;
				s_44 = s_41;
				s_45 = (short) (rsbytebuffer_4.readSignedSmart() + s_46);
				s_46 = s_45;
				this.faceA[i_39] = s_43;
				this.faceB[i_39] = s_41;
				this.faceC[i_39] = s_45;
				if (s_45 > this.maxVertexUsed) {
					this.maxVertexUsed = s_45;
				}
			}
		}

		++this.maxVertexUsed;
		rsbytebuffer_4.offset = i_28;

		for (i_39 = 0; i_39 < this.texturedFaceCount; i_39++) {
			this.textureRenderTypes[i_39] = 0;
			this.textureTriangleX[i_39] = (short) rsbytebuffer_4.readUnsignedShort();
			this.textureTriangleY[i_39] = (short) rsbytebuffer_4.readUnsignedShort();
			this.textureTriangleZ[i_39] = (short) rsbytebuffer_4.readUnsignedShort();
		}

		if (this.faceTextureIndexes != null) {
			boolean bool_47 = false;

			for (i_40 = 0; i_40 < this.faceCount; i_40++) {
				int i_48 = this.faceTextureIndexes[i_40] & 0xff;
				if (i_48 != 255) {
					if (this.faceA[i_40] == (this.textureTriangleX[i_48] & 0xffff) && this.faceB[i_40] == (this.textureTriangleY[i_48] & 0xffff) && this.faceC[i_40] == (this.textureTriangleZ[i_48] & 0xffff)) {
						this.faceTextureIndexes[i_40] = -1;
					} else {
						bool_47 = true;
					}
				}
			}

			if (!bool_47) {
				this.faceTextureIndexes = null;
			}
		}

		if (!bool_3) {
			this.faceTextures = null;
		}

		if (!bool_2) {
			this.faceRenderTypes = null;
		}

	}

	public void upscale() {
		int i_2;
		for (i_2 = 0; i_2 < this.vertexCount; i_2++) {
			this.vertexX[i_2] <<= 2;
			this.vertexY[i_2] <<= 2;
			this.vertexZ[i_2] <<= 2;
		}

		if (this.texturedFaceCount > 0 && this.textureScaleX != null) {
			for (i_2 = 0; i_2 < this.textureScaleX.length; i_2++) {
				this.textureScaleX[i_2] <<= 2;
				this.textureScaleY[i_2] <<= 2;
				if (this.textureRenderTypes[i_2] != 1) {
					this.textureScaleZ[i_2] <<= 2;
				}
			}
		}

	}
	
	public static RSModel getMesh(Store store, int meshId, boolean rs3) {
		byte[] data = store.getIndex(IndexType.MODELS).getFile(meshId, 0);
		if (data == null)
			return null;
		RSModel model = new RSModel(data, rs3);
		model.id = meshId;
		return model;
	}

	public static RSModel getMesh(int meshId) {
		return getMesh(Cache.STORE, meshId, false);
	}

	public void translate(int dx, int dy, int dz) {
		for (int i = 0; i < this.vertexCount; i++) {
			this.vertexX[i] += dx;
			this.vertexY[i] += dy;
			this.vertexZ[i] += dz;
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
	
	private static Map<Integer, Integer> PARTICLE_CONVERTS = new HashMap<>();
	private static Map<Integer, Integer> MAGNET_CONVERTS = new HashMap<>();
	
	public byte[] convertTo727(Store to, Store from) throws IOException {
		if (faceTextures != null) {
			for (int index = 0; index < faceTextures.length; index++) {
				int texture = faceTextures[index];
				if (texture != -1) {
					try {
						faceTextures[index] = (short) TextureConverter.convert(from, to, texture, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (emitterConfigs != null) {
			for (int i = 0;i < emitterConfigs.length;i++) {
				if (to.getIndex(IndexType.PARTICLES).getFile(0, emitterConfigs[i].type) != null)
					continue;
				if (PARTICLE_CONVERTS.get(emitterConfigs[i].type) != null) {
					emitterConfigs[i].type = PARTICLE_CONVERTS.get(emitterConfigs[i].type);
					continue;
				}
				int newId = to.getIndex(IndexType.PARTICLES).getLastFileId(0)+1;
				ParticleProducerDefinitions rs3 = new ParticleProducerDefinitions();
				rs3.decode(from.getIndex(IndexType.PARTICLES).getFile(0, emitterConfigs[i].type), true);
				rs3.id = newId; //812 = dung cape, 744 = comp cape
				rs3.textureId = 744; //TextureConverter.convert(from, to, rs3.textureId, true);
				rs3.write(to);
				System.out.println("Packed emitter def from " + emitterConfigs[i].type + " to " + newId);
				PARTICLE_CONVERTS.put(emitterConfigs[i].type, newId);
				emitterConfigs[i].type = newId;
			}
		}
		if (magnetConfigs != null) {
			for (int i = 0;i < magnetConfigs.length;i++) {
				if (to.getIndex(IndexType.PARTICLES).getFile(1, magnetConfigs[i].type) != null)
					continue;
				if (MAGNET_CONVERTS.get(magnetConfigs[i].type) != null) {
					magnetConfigs[i].type = MAGNET_CONVERTS.get(magnetConfigs[i].type);
					continue;
				}
				int newId = to.getIndex(IndexType.PARTICLES).getLastFileId(1)+1;
				to.getIndex(IndexType.PARTICLES).putFile(1, newId, from.getIndex(IndexType.PARTICLES).getFile(1, magnetConfigs[i].type));
				System.out.println("Packed magnet def from " + magnetConfigs[i].type + " to " + newId);
				MAGNET_CONVERTS.put(magnetConfigs[i].type, newId);
				magnetConfigs[i].type = newId;
			}
		}
//		if (billBoardConfigs != null) {
//			
//		}
		return encode();
	}
}
