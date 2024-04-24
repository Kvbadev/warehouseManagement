package com.kvbadev.wms.models.warehouse;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "racks")
public class Rack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
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
