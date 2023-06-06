package com.kvbadev.wms.models.warehouse;

import java.util.List;

public class StorageRack implements StorageUnit {
    private Float width;
    private Float height;
    private Float depth;
    private List<StorageShelf> shelves;

    @Override
    public Float getWidth() {
        return null;
    }

    @Override
    public Float getDepth() {
        return null;
    }

    @Override
    public Float getHeight() {
        return null;
    }
}
