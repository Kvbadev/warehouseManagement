package com.kvbadev.wms.models.warehouse;

import javax.vecmath.Matrix3d;
import java.util.List;

public class StorageShelf implements StorageUnit {
    private Float width;
    private Float depth;
    private Float height;
    private List<Package> packages;
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

    public Boolean[][][] getFreeSpace() {
        return null;
    }

    public void appendPackage(Package p) {
        packages.add(p);
    }

    public void removePackage(int Id) throws Exception {
        var p = packages.stream().filter(pkg -> pkg.getId() == Id).findFirst();
        if(p.isEmpty()) throw new Exception("Package with this id: ${ hasn't been found");
        packages.remove(p.get());
    }

    public boolean containsPackage(int Id) {
        return packages.stream().anyMatch(pkg -> pkg.getId() == Id);
    }
}
