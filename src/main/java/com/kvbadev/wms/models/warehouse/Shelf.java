package com.kvbadev.wms.models.warehouse;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "shelves")
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    @NotNull
    private String name;
    @NotNull
    @Positive
    private Integer pos;
    @NotNull
    @Positive
    private Integer workingLoadLimit; //in grams
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rack_id")
    private Rack rack;

    public Shelf() {
    }

    public Shelf(int id, String name, int pos, int workingLoadLimit) {
        this.id = id;
        this.name = name;
        this.pos = pos;
        this.workingLoadLimit = workingLoadLimit;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPos() {
        return pos;
    }

    public Integer getWorkingLoadLimit() {
        return workingLoadLimit;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public void setWorkingLoadLimit(Integer workingLoadLimit) {
        this.workingLoadLimit = workingLoadLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return id == shelf.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }
}
