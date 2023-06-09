package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "rack_routes")
@Entity
public class RackRoute {
    @GeneratedValue
    @Id
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_rack_id", insertable = false, updatable = false)
    private StorageRack source;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_rack_id", insertable = false, updatable = false)
    private StorageRack destination;
    private double weight;

    public RackRoute(StorageRack source, StorageRack destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
    public RackRoute() {
    }

    public StorageRack getSource() {
        return source;
    }

    public StorageRack getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RackRoute that = (RackRoute) o;
        return Double.compare(that.weight, weight) == 0 && Objects.equals(source, that.source) && Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination, weight);
    }

    public void setSource(StorageRack route) {
        this.source = route;
    }

    public void setDestination(StorageRack route) {
        this.destination = route;
    }
}
