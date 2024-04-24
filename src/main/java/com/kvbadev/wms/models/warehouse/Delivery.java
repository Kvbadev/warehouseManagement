package com.kvbadev.wms.models.warehouse;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "arrival_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;
    @Column(name = "has_arrived")
    private Boolean hasArrived;

    public Delivery() {
    }

    public Delivery(LocalDate arrivalDate, Boolean hasArrived) {
        this.arrivalDate = arrivalDate;
        this.hasArrived = hasArrived;
    }

    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Boolean getHasArrived() {
        return hasArrived;
    }

    public void setHasArrived(Boolean hasArrived) {
        this.hasArrived = hasArrived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery delivery)) return false;
        return Objects.equals(id, delivery.id) && Objects.equals(arrivalDate, delivery.arrivalDate) && Objects.equals(hasArrived, delivery.hasArrived);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, arrivalDate, hasArrived);
    }
}
