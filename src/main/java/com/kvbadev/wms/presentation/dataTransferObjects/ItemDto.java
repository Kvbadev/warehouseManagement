package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ItemDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    private final String name;
    @Size(max = 255, message = "Description length must not be greater than 255 characters")
    private final String description;
    @Positive(message = "Quantity must be greater than 0")
    @NotNull
    private final Integer quantity;
    @Positive(message = "Net price must be greater than 0")
    @NotNull
    private final Long netPrice;
    private final Integer parcelId;

    @ConstructorProperties({"name","description","quantity","netPrice","parcelId"})
    @JsonCreator
    public ItemDto(String name, String description, @NotNull Integer quantity, @NotNull Long netPrice, Integer parcelId) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.netPrice = netPrice;
        this.parcelId = parcelId;
    }

    public @NotNull String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public @NotNull Integer getQuantity() {
        return quantity;
    }

    public @NotNull Long getNetPrice() {
        return netPrice;
    }

    public Integer getParcelId() {
        return parcelId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto that = (ItemDto) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(quantity, that.quantity) && Objects.equals(netPrice, that.netPrice) && Objects.equals(parcelId, that.parcelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, quantity, netPrice, parcelId);
    }


}
