package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query(value = "SELECT * FROM items i WHERE i.parcel_id = :id", nativeQuery = true)
    List<Item> findItemsByParcelId(@Param("id") int parcelId);

    @Query(value = "SELECT * FROM items i WHERE i.delivery_id = :id", nativeQuery = true)
    List<Item> findItemsByDeliveryId(@Param("id") int delivery_id);
}
