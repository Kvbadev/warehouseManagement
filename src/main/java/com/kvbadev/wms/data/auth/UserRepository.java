package com.kvbadev.wms.data.auth;

import com.kvbadev.wms.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
}