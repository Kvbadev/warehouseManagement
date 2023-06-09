package com.kvbadev.wms.data;

import com.kvbadev.wms.models.warehouse.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
