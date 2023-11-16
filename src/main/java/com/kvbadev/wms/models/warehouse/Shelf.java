package com.kvbadev.wms.models.warehouse;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "shelves")
public class Shelf implements Comparable<Shelf> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private int pos;
    private int workingLoadLimit; //in grams
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

    public int getPos() {
        return pos;
    }

    public int getWorkingLoadLimit() {
        return workingLoadLimit;
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

    @Override
    public int compareTo(Shelf that) {
        if(this.pos == that.pos) return 0;
        return this.pos < that.pos ? -1 : 1;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }
}
