package com.kvbadev.wms.data.auth;

import com.kvbadev.wms.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}