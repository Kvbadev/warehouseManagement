package com.kvbadev.wms.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryDetail {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalDate;

    public DeliveryDetail(Integer id, Date arrivalDate) {
        this.id = id;
        this.arrivalDate = arrivalDate;
    }

    public DeliveryDetail(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
