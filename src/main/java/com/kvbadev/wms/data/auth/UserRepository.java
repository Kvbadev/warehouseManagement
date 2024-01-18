package com.kvbadev.wms.data.auth;

import com.kvbadev.wms.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    //The column name id was not found in this ResultSet
    @Query(value = "SELECT u.* FROM users u "
                   + "JOIN users_roles ur ON u.id = ur.user_id "
                   + "JOIN roles r ON ur.role_id = r.id "
                   + "WHERE r.name LIKE :role",
           nativeQuery = true)
    List<User> findByRole(@Param("role") String role);

    @Modifying
    @Query(nativeQuery = true,
        value = "INSERT INTO users_roles (user_id,role_id) VALUES (:userId, :roleId)")
    void addUserToRole(@Param("userId") int userId, @Param("roleId") int roleId);
}