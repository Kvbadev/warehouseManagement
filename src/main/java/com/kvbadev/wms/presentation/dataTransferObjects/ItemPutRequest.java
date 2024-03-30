package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ItemPutRequest extends ItemDto{
    private final Integer id;
    @JsonCreator
    @ConstructorProperties({"id","name","description","quantity","netPrice","parcelId"})
    public ItemPutRequest(Integer id, @NotNull String name, String description, @NotNull Integer quantity, @NotNull Long netPrice, Integer parcelId) {
        super(name, description, quantity, netPrice, parcelId);
        this.id = id;
    }
    public Integer getId() {
        return id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ItemPutRequest that = (ItemPutRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
