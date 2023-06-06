package com.kvbadev.wms.models.warehouse;

import com.kvbadev.wms.models.Product;
import org.apache.commons.collections.list.UnmodifiableList;

import javax.vecmath.Matrix3d;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Package implements StorageUnit {
    private Integer id;
    private List<Product> products;
    private Float width;
    private Float depth;
    private Float height;

    public Integer getId() {
        return id;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public Product getProduct(int Id) throws Exception {
        var p = products.stream().filter(prod -> prod.getId() == Id).findFirst();
        if(p.isEmpty()) throw new Exception("Package with this id: hasn't been found");
        return p.get();
    }

    @Override
    public Float getWidth() {
        return width;
    }

    @Override
    public Float getDepth() {
        return depth;
    }

    @Override
    public Float getHeight() {
        return height;
    }
}
