package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Integer> {
    @Query(value = "SELECT p.* FROM items i JOIN parcels p ON i.parcel_id = p.id WHERE i.id = :itemId", nativeQuery = true)
    Optional<Parcel> findByItemId(@Param("itemId") int itemId);

}
