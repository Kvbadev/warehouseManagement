package com.kvbadev.wms.services;

import com.kvbadev.wms.data.security.RoleRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.models.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {
    @Autowired
    private RoleRepository roleRepository;

    public void addRolesByRolesId(User user, List<Integer> rolesId) {
        for(int id : rolesId) {
            roleRepository.findById(id)
                    .ifPresentOrElse(
                            user::addRole,
                            () -> {
                                throw new EntityNotFoundException(Role.class, id);
                            }
                    );
        }
    }
}
