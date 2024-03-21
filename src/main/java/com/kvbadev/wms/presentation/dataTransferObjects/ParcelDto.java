package com.kvbadev.wms.presentation.dataTransferObjects;

import java.util.Objects;

public class ParcelDto {
    private String name;
    private Integer weight;
    private Integer deliveryId;
    private Integer shelfId;

    public ParcelDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParcelDto parcelDto)) return false;
        return weight == parcelDto.weight && Objects.equals(name, parcelDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight);
    }
}
