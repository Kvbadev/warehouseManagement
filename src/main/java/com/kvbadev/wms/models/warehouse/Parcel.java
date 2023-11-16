package com.kvbadev.wms.models.warehouse;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "parcels")
public class Parcel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String name;
    private int weight; //weight in grams

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    public Parcel() {
    }

    public Parcel(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public void addItem(Item item) {
        item.setParcel(this);
    }

    public void removeItem(Item item) {
        item.setParcel(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel aParcel = (Parcel) o;
        return id == aParcel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
