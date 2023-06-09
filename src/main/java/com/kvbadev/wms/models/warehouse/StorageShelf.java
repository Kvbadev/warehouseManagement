package com.kvbadev.wms.models.warehouse;

import javax.persistence.*;
import java.util.Set;

@Table(name = "storage_shelves")
@Entity
@AttributeOverride(name="id", column = @Column(name="storage_shelf_id"))
public class StorageShelf extends StorageUnit {
    @Enumerated(EnumType.ORDINAL)
    private final StorageType type;

    @ManyToOne
    @JoinColumn(name = "storage_rack_id")
    private StorageRack storageRack;
    @OneToMany
    private Set<Parcel> parcelList;

    public StorageShelf(StorageType type, Set<Parcel> parcelList) {
        this.type = type;
        this.parcelList = parcelList;
    }

    public StorageShelf(StorageType type) {
        this.type = type;
    }

    public Boolean[][][] getFreeSpace() {
        return null;
    }

    public void appendPackage(Parcel p) {
        parcelList.add(p);
    }

    public void removePackage(int Id) throws Exception {
        var p = parcelList.stream().filter(pkg -> pkg.getId() == Id).findFirst();
        if (p.isEmpty()) throw new Exception("Package with this id: ${ hasn't been found");
        parcelList.remove(p.get());
    }

    public boolean containsPackage(int Id) {
        return parcelList.stream().anyMatch(pkg -> pkg.getId() == Id);
    }
}
