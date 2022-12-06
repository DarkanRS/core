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
package com.rs.lib.game;

import com.rs.lib.util.MapUtils;
import com.rs.lib.util.MapUtils.Structure;
import com.rs.lib.util.Utils;

public record WorldTile(short x, short y, byte plane) {

	public static WorldTile of(int x, int y, int plane) {
		return new WorldTile((short) x, (short) y, (byte) plane);
	}
	
	public static WorldTile of(int x, int y, int plane, int size) {
		return new WorldTile((short) getCoordFaceX(x, size, size, -1), (short) getCoordFaceY(y, size, size, -1), (byte) plane);
	}

	public static WorldTile of(WorldTile tile) {
		return new WorldTile(tile.x, tile.y, tile.plane);
	}

	public static WorldTile of(WorldTile tile, int randomize) {
		return new WorldTile((short) (tile.x + Utils.random(randomize * 2 + 1) - randomize), (short) (tile.y + Utils.random(randomize * 2 + 1) - randomize), tile.plane);
	}

	public static WorldTile of(int hash) {
		return new WorldTile((short) (hash >> 14 & 0x3fff), (short) (hash & 0x3fff), (byte) (hash >> 28));
	}
	
	public static WorldTile of(int z, int regionX, int regionY, int localX, int localY) {
		return new WorldTile((short) (regionX << 6 | localX), (short) (regionY << 6 | localY), (byte) z);
	}

	public boolean isAt(int x, int y) {
		return this.x == x && this.y == y;
	}

	public boolean isAt(int x, int y, int z) {
		return this.x == x && this.y == y && this.plane == z;
	}

	public int getX() {
		return x;
	}

	public int getXInRegion() {
		return x & 0x3F;
	}

	public int getYInRegion() {
		return y & 0x3F;
	}

	public int getXInChunk() {
		return x & 0x7;
	}

	public int getYInChunk() {
		return y & 0x7;
	}

	public int getY() {
		return y;
	}

	public int getPlane() {
		if (plane > 3)
			return 3;
		return plane;
	}

	public int getChunkX() {
		return (x >> 3);
	}
	
	public int getChunkId() {
		return MapUtils.encode(Structure.CHUNK, getChunkX(), getChunkY());
	}
	
	public int getChunkXInScene(int chunkId) {
		return getChunkX() - MapUtils.decode(Structure.CHUNK, chunkId)[0];
	}

	public int getChunkYInScene(int chunkId) {
		return getChunkY() - MapUtils.decode(Structure.CHUNK, chunkId)[1];
	}

	public int getXInScene(int chunkId) {
		return getX() - MapUtils.decode(Structure.CHUNK, chunkId)[0] * 8;
	}

	public int getYInScene(int chunkId) {
		return getY() - MapUtils.decode(Structure.CHUNK, chunkId)[1] * 8;
	}

	public int getChunkY() {
		return (y >> 3);
	}

	public int getRegionX() {
		return (x >> 6);
	}

	public int getRegionY() {
		return (y >> 6);
	}

	public int getRegionId() {
		return ((getRegionX() << 8) + getRegionY());
	}

	public int getRegionHash() {
		return getRegionY() + (getRegionX() << 8) + (plane << 16);
	}
	
	public static int toInt(int x, int y, int plane) {
		return y + (x << 14) + (plane << 28);
	}

	public int getTileHash() {
		return y + (x << 14) + (plane << 28);
	}
	
	@Override
	public int hashCode() {
		return getTileHash();
	}

	public boolean withinDistance(WorldTile tile, int distance) {
		if (tile.plane != plane)
			return false;
		int deltaX = tile.x - x, deltaY = tile.y - y;
		return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
	}

	public boolean withinDistance(WorldTile tile) {
		return withinDistance(tile, 14);
	}

	public int getCoordFaceX(int sizeX) {
		return getCoordFaceX(sizeX, -1, -1);
	}

	public static final int getCoordFaceX(int x, int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public static final int getCoordFaceY(int y, int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}

	public int getCoordFaceX(int sizeX, int sizeY, int rotation) {
		return x + ((rotation == 1 || rotation == 3 ? sizeY : sizeX) - 1) / 2;
	}

	public int getCoordFaceY(int sizeY) {
		return getCoordFaceY(-1, sizeY, -1);
	}

	public int getCoordFaceY(int sizeX, int sizeY, int rotation) {
		return y + ((rotation == 1 || rotation == 3 ? sizeX : sizeY) - 1) / 2;
	}
	
	public int getLongestDelta(WorldTile other) {
		int deltaX = Math.abs(getX() - other.getX());
		int deltaY = Math.abs(getY() - other.getY());
		return Math.max(deltaX, deltaY);
	}
	
	public WorldTile transform(int x, int y) {
		return transform(x, y, 0);
	}

	public WorldTile transform(int x, int y, int plane) {
		return WorldTile.of(this.x + x, this.y + y, this.plane + plane);
	}

	public boolean matches(WorldTile other) {
		return x == other.x && y == other.y && plane == other.plane;
	}

	public boolean withinArea(int a, int b, int c, int d) {
		return getX() >= a && getY() >= b && getX() <= c && getY() <= d;
	}

	@Override
	public String toString() {
		return "[ X: " + x + ", Y: " + y + ", Z: " + plane + " ]";
	}
}
