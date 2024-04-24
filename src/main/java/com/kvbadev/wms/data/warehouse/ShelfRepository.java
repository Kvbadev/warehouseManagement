package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfRepository extends JpaRepository<Shelf, Integer> {
}
