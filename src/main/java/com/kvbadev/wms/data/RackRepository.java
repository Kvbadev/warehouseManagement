package com.kvbadev.wms.data;

import com.kvbadev.wms.models.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<Rack, Integer> {
}
