package com.kvbadev.wms.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "storage_rooms")
public class StorageRoom {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    public StorageRoom() {
    }
    public StorageRoom(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void addRack(Rack rack) {
        rack.setStorageRoom(this);
    }

    public void removeRack(Rack rack) {
        rack.setStorageRoom(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageRoom that = (StorageRoom) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
