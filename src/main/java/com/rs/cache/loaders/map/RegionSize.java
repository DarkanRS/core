package com.rs.cache.loaders.map;

public enum RegionSize {
    SIZE_104(104),
    SIZE_120(120),
    SIZE_136(136),
    SIZE_168(168),
    SIZE_72(72);

    public int size;

    RegionSize(int size) {
        this.size = size;
    }
}
