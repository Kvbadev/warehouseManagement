package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<Rack, Integer> {
}
