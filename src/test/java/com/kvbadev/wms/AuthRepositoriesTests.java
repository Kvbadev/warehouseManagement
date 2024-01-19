package com.kvbadev.wms;

import com.kvbadev.wms.data.security.PrivilegeRepository;
import com.kvbadev.wms.data.security.RoleRepository;
import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.models.security.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
public class AuthRepositoriesTests {
    @Autowired
    PrivilegeRepository privilegeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void getUsersRoles() {
        User u = userRepository.save(new User("test", "test", "test", "test"));
        Role r = roleRepository.save(new Role("ROLE_ADMIN"));
        userRepository.addUserToRole(u.getId(), r.getId());

        List<Role> roles = roleRepository.findByUser(u.getEmail());
        assert roles.get(0).getName().equals("ROLE_ADMIN");
    }

}
