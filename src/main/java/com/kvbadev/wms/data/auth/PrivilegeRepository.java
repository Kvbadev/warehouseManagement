package com.kvbadev.wms.data.auth;

import com.kvbadev.wms.models.auth.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Privilege findByName(String name);
}