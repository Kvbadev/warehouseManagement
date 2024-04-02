package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

public class DeliveryPutRequest extends DeliveryDto{
    private final Integer id;

    @JsonCreator
    @ConstructorProperties({"id","arrivalDate"})
    public DeliveryPutRequest(Integer id, @NotNull LocalDate arrivalDate) {
        super(arrivalDate, false);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryPutRequest that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
