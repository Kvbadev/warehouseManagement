package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    @Query(value = "SELECT * FROM deliveries ORDER BY arrival_date", nativeQuery = true)
    List<Delivery> findLatest();

    @Query(value = "SELECT d.* FROM deliveries d JOIN parcels p ON d.id = p.delivery_id WHERE p.id = id",
            nativeQuery = true)
    Optional<Delivery> findByParcelId(@Param("id") int parcelId);
}
