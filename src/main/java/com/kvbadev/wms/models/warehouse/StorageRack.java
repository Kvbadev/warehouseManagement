package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.List;

@Table(name = "storage_racks")
@Entity
@AttributeOverride(name="id", column = @Column(name="storage_rack_id"))
public class StorageRack extends StorageUnit {
    @OneToMany(mappedBy = "storageRack")
    private List<StorageShelf> shelves;
    @OneToMany(mappedBy = "destination")
    private List<RackRoute> routes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public void addRoute(StorageRack destination, double weight) {
        var route = new RackRoute(this, destination, weight);
        routes.add(route);
    }

    public void removeRoute(StorageRack destination) {
        var route = findRoute(destination);
        if(route != null) {
            route.setSource(null);
            route.setDestination(null);
            routes.remove(route);
        }
    }

    public RackRoute findRoute(StorageRack destination) {
        return this.routes.stream().filter(r -> r.getDestination() == destination).findFirst().orElse(null);
    }

}
