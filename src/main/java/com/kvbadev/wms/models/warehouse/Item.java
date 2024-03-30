package com.kvbadev.wms.models.warehouse;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "items")
public class Item {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;
    @Column(name = "net_price")
    @Positive(message = "Net price must be greater than 0")
    private Long netPrice;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id")
    private Parcel parcel;

    public Item() {
    }

    public Item(String name, String description, Integer quantity, Long netPrice) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.netPrice = netPrice;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getNetPrice() {
        return netPrice;
    }
    public void setNetPrice(Long netPrice) {
        this.netPrice = netPrice;
    }
    @JsonIgnore
    public BigDecimal getNormalizedNetPrice() {
        return new BigDecimal(netPrice).movePointLeft(2);
    }
    public Parcel getParcel() {
        return this.parcel;
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
