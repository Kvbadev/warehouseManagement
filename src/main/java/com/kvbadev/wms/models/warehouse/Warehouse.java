package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "warehouses")
@Entity
public class Warehouse {
    @GeneratedValue
    @Id
    private long id;
    @OneToMany(mappedBy = "warehouse")
    private final List<StorageRack> rackList = new ArrayList<>();

    public Warehouse() {
    }

    public long getId() {
        return id;
    }

    public List<StorageRack> rackList() {
        return rackList;
    }

    public void addStorageRack(StorageRack rack) {
        rack.setWarehouse(this);
        rackList.add(rack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return id == warehouse.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
