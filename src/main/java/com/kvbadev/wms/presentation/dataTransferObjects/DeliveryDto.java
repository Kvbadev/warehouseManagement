package com.kvbadev.wms.presentation.dataTransferObjects;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Objects;

public class DeliveryDto {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    public DeliveryDto() {}

    public DeliveryDto(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
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
