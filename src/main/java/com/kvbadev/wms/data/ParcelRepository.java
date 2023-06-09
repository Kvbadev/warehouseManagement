package com.kvbadev.wms.data;

import com.kvbadev.wms.models.warehouse.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
}
