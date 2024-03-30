package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ParcelDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private final String name;
    @Positive(message = "Weight must be greater than 0")
    @NotNull
    private final Integer weight; //weight in grams
    private final Integer deliveryId;
    private final Integer shelfId;

    @JsonCreator
    @ConstructorProperties({"name","weight","deliveryId","shelfId"})
    public ParcelDto(String name, @NotNull Integer weight, Integer deliveryId, Integer shelfId) {
        this.name = name;
        this.weight = weight;
        this.deliveryId = deliveryId;
        this.shelfId = shelfId;
    }

    public String getName() {
        return name;
    }

    public @NotNull Integer getWeight() {
        return weight;
    }

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public Integer getShelfId() {
        return shelfId;
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
