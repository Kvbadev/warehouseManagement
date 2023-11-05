package com.kvbadev.wms.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "racks")
public class Rack {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_room_id")
    private StorageRoom storageRoom;

    public Rack() {

    }
    public Rack(String name) {
        this.name = name;
//        this.shelves = new TreeSet<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public SortedSet<Shelf> getShelves() {
//        return Collections.unmodifiableSortedSet(shelves);
//    }

    public void addShelf(Shelf shelf) {
//        shelves.add(shelf);
        shelf.setRack(this);
    }

    public void removeShelf(Shelf shelf) {
//        shelves.remove(shelf);
        shelf.setRack(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rack rack = (Rack) o;
        return id == rack.id;
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
