package com.kvbadev.wms;

import com.kvbadev.wms.data.auth.PrivilegeRepository;
import com.kvbadev.wms.data.auth.RoleRepository;
import com.kvbadev.wms.data.auth.UserRepository;
import com.kvbadev.wms.models.auth.Privilege;
import com.kvbadev.wms.models.auth.Role;
import com.kvbadev.wms.models.auth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false; //if multiple instances of the server are launched

    String[] privileges = {"READ_PRIVILEGE", "WRITE_PRIVILEGE"};
    String[] roles = {"ROLE_ADMIN", "ROLE_USER"};

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        Logger logger = LoggerFactory.getLogger(SetupDataLoader.class);

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        if(userRepository.count() > 0) {
            alreadySetup = true;
            return;
        }
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        String passwd = passwordEncoder.encode("test");
        logger.warn("password: "+passwd);

        User user = new User(
                "Test",
                "Test",
                "test@test.com",
                passwd,
                Arrays.asList(adminRole)
        );
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
