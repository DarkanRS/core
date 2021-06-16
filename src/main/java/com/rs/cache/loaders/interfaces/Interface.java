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
