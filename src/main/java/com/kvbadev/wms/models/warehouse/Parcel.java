package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "parcels")
@Entity
@AttributeOverride(name="id", column = @Column(name="parcel_id"))
public class Parcel extends StorageUnit {

    @OneToMany(mappedBy = "parcel")
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        product.setParcel(this);
        products.add(product);
    }

    public void removeProduct(Product product) {
        product.setParcel(null);
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }
}
