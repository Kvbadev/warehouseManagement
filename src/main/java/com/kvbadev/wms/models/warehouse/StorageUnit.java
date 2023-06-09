package com.kvbadev.wms.models.warehouse;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class StorageUnit {

    @GeneratedValue
    @Id
    protected long id;
    protected Float width;
    protected Float height;
    protected Float depth;
    protected StorageType type;

    public Float getWidth() {
        return width;
    }

    public Float getDepth() {
        return depth;
    }

    public Float getHeight() {
        return height;
    }

    public StorageType getType() {
        return type;
    }

    public long getId() {
        return id;
    }
}
