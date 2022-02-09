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
package com.rs.cache.loaders.interfaces;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;

import javax.swing.JComponent;

import com.rs.cache.Store;

public class Interface {

	public int id;
	public Store cache;
	public IComponentDefinitions[] components;
	public JComponent[] jcomponents;

	public Interface(int id, Store cache) {
		this(id, cache, true);
	}

	public Interface(int id, Store cache, boolean load) {
		this.id = id;
		this.cache = cache;
		if (load)
			components = IComponentDefinitions.getInterface(id);
	}

	public void draw(JComponent parent) {
		
	}

	public Image resizeImage(Image image, int width, int height, Component c) {
		ImageFilter replicate = new ReplicateScaleFilter(width, height);
		ImageProducer prod = new FilteredImageSource(image.getSource(), replicate);
		return c.createImage(prod);
	}
}
