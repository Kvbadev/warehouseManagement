package com.kvbadev.wms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item {
    @GeneratedValue
    @Id
    private int id;
    private String name;
    private String description;
    private int quantity;
    @Column(name = "net_price")
    private long netPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    public Item() {
    }

    public Item(String name, String description, long netPrice) {
        this.name = name;
        this.description = description;
        this.quantity = 1;
        this.netPrice = netPrice;
    }
    public Item(String name, String description, int quantity, long netPrice) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.netPrice = netPrice;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getNetPrice() {
        return netPrice;
    }
    public void setProductNetPrice(long netPrice) {
        this.netPrice = netPrice;
    }
    @JsonIgnore
    public BigDecimal getNormalizedNetPrice() {
        return new BigDecimal(netPrice).movePointLeft(2);
    }
    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && quantity == item.quantity && Objects.equals(name, item.name) && Objects.equals(description, item.description) && Objects.equals(netPrice, item.netPrice) && Objects.equals(parcel, item.parcel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, quantity, netPrice, parcel);
    }

}
