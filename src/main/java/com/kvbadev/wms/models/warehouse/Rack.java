package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "racks")
public class Rack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_room_id")
    private StorageRoom storageRoom;

    public Rack() {

    }
    public Rack(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addShelf(Shelf shelf) {
        shelf.setRack(this);
    }

    public void removeShelf(Shelf shelf) {
        shelf.setRack(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rack rack = (Rack) o;
        return Objects.equals(id, rack.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public StorageRoom getStorageRoom() {
        return storageRoom;
    }

    public void setStorageRoom(StorageRoom storageRoom) {
        this.storageRoom = storageRoom;
    }
}
