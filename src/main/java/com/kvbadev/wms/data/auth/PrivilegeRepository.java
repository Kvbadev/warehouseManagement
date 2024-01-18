package com.kvbadev.wms.data.auth;

import com.kvbadev.wms.models.auth.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Privilege findByName(String name);
    @Query(nativeQuery = true,
            value = "SELECT p.* from privileges p JOIN roles_privileges rp ON p.id = rp.privilege_id " +
                    "JOIN roles r ON rp.role_id = r.id " +
                    "WHERE r.name LIKE :roleName")
    List<Privilege> findByRole(@Param("roleName") String roleName);
}