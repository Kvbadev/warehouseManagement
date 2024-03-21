package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    @Query(value = "SELECT * FROM deliveries ORDER BY arrival_date", nativeQuery = true)
    List<Delivery> findLatestDeliveries();

    @Query(value = "SELECT DISTINCT d.* from deliveries d JOIN delivery_items di ON d.id = di.delivery_id WHERE di.item_id = :id",
            nativeQuery = true)
    List<Delivery> findDeliveriesByItemId(@Param("id") int id);

    @Query(value = "SELECT d.* FROM deliveries JOIN items i on id = i.delivery_id WHERE i.delivery_id = :id", nativeQuery = true)
    Delivery findByItemId(@Param("id") int itemId);
}
