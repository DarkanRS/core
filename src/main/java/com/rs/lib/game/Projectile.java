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

public class Projectile {
	
	protected WorldTile from, to;
	protected int sourceId;
	protected int lockOnId;
	private boolean useTerrainHeight;
	private int spotAnimId, startHeight, endHeight, startTime, endTime, slope, angle;
	protected int fromSizeX, fromSizeY;
	protected int toSizeX, toSizeY;
	private int basFrameHeightAdjust = -1;
	
	public Projectile(WorldTile from, WorldTile to, int spotAnimId, int startHeight, int endHeight, int startTime, int endTime, int slope, int angle) {
		this.from = from;
		this.to = to;
		this.spotAnimId = spotAnimId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slope = slope;
		this.angle = angle;
	}
	
	public Projectile setBASFrameHeightAdjust(int frameIndex) {
		basFrameHeightAdjust = frameIndex;
		return this;
	}
	
	public int getBASFrameHeightAdjust() {
		return basFrameHeightAdjust;
	}
	
	public Projectile setUseTerrainHeight() {
		useTerrainHeight = true;
		return this;
	}

	public boolean usesTerrainHeight() {
		return useTerrainHeight;
	}

	public int getSpotAnimId() {
		return spotAnimId;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public int getSlope() {
		return slope;
	}

	public int getAngle() {
		return angle;
	}

	public int getSourceId() {
		return sourceId;
	}

	public int getLockOnId() {
		return lockOnId;
	}
	
	public WorldTile getSource() {
		return new WorldTile(from.getX() + fromSizeX, from.getY() + fromSizeY, from.getPlane());
	}
	
	public WorldTile getDestination() {
		return new WorldTile(to.getX() + toSizeX, to.getY() + toSizeY, to.getPlane());
	}

}
