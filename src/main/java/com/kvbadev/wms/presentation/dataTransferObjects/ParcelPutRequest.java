package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ParcelPutRequest extends ParcelDto {
    private final Integer id;

    @JsonCreator
    @ConstructorProperties({"id","name","weight","deliveryId","shelfId"})
    public ParcelPutRequest(Integer id, String name, Integer weight, Integer deliveryId, Integer shelfId) {
        super(name, weight, deliveryId, shelfId);
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
        ParcelPutRequest that = (ParcelPutRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
