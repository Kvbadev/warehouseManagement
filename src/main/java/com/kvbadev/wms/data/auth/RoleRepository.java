package com.kvbadev.wms.data.auth;

import com.kvbadev.wms.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
    @Query(nativeQuery = true,
        value = "SELECT r.* from roles r JOIN users_roles ur ON r.id = ur.role_id " +
                "JOIN users u ON ur.user_id = u.id " +
                "WHERE u.email LIKE :email")
    List<Role> findByUser(@Param("email") String email);

}