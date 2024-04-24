package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.Objects;

public class DeliveryDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private final LocalDate arrivalDate;
    @NotNull
    private final Boolean hasArrived;

    @JsonCreator
    @ConstructorProperties({"arrivalDate","hasArrived"})
    public DeliveryDto(@NotNull LocalDate arrivalDate, @NotNull Boolean hasArrived) {
        this.arrivalDate = arrivalDate;
        this.hasArrived = hasArrived;
    }

    public @NotNull LocalDate getArrivalDate() {
        return arrivalDate;
    }
    public @NotNull Boolean getHasArrived() {
        return hasArrived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryDto that)) return false;
        return Objects.equals(arrivalDate, that.arrivalDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arrivalDate);
    }

}
