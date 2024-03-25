package com.kvbadev.wms.models.warehouse;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "arrival_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate arrivalDate;

    public Delivery() {
    }

    public Delivery(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
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
}
