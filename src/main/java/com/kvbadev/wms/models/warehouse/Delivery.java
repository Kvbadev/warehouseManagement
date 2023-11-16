package com.kvbadev.wms.models.warehouse;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

//The delivery class. The relationship between the delivery and the items in it is saved in a join table - delivery_items
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "arrival_date")
    private Date arrivalDate;

    public Delivery() {
    }

    public Delivery(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Integer getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
