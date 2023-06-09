package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.List;

@Table(name = "warehouses")
@Entity
public class Warehouse {
    @GeneratedValue
    @Id
    private long id;
    @OneToMany(mappedBy = "warehouse")
    private List<StorageRack> storageRacks;

    public long getId() {
        return id;
    }

    public List<StorageRack> getStorageRacks() {
        return storageRacks;
    }

    public void setStorageRacks(List<StorageRack> storageRacks) {
        this.storageRacks = storageRacks;
    }
}
