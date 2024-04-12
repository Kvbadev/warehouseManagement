package com.kvbadev.wms.services;

import com.kvbadev.wms.data.security.RoleRepository;
import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.exceptions.DuplicateResourceException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.dataTransferObjects.UserDto;
import com.kvbadev.wms.presentation.dataTransferObjects.UserPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public void addRolesByRoleNames(User user, List<String> roleNames) {
        for(String name : roleNames) {
            roleRepository.findByName(name)
                    .ifPresentOrElse(
                            user::addRole,
                            () -> {
                                throw new EntityNotFoundException(Role.class, name, "name");
                            }
                    );
        }
    }
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    public User saveUser(UserDto user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()) {
            throw new DuplicateResourceException(User.class, "email", user.getEmail());
        }
        User newUser = userMapper.userDtoToUser(user);

        String newPassword = hashPassword(user.getPassword());
        newUser.setPassword(newPassword);

        Boolean enabled = true;
        newUser.setEnabled(enabled);

        //add roles if any exist
        if (user.getRoleNames() != null) {
            addRolesByRoleNames(newUser, user.getRoleNames());
        }
        return userRepository.save(newUser);
    }
    public User updateUser(UserDto updateRequest, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));

        //update shallow values
        User userRequest = userMapper.userDtoToUser(updateRequest);
        userMapper.update(user, userRequest);

        //update roles
        if (updateRequest.getRoleNames() != null) {
            addRolesByRoleNames(user, updateRequest.getRoleNames());
        }
        return userRepository.save(user);
    }

    public User updateUser(UserPutRequest updateRequest) {
        if (updateRequest.getId() != null) {
            //if the resource already exists, change response to OK from CREATED
            userRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException(User.class, updateRequest.getId()));
        }

        User user = userMapper.userPutToUser(updateRequest);

        //add roles if any exist
        if (updateRequest.getRoleNames() != null) {
            addRolesByRoleNames(user, updateRequest.getRoleNames());
        }

        return userRepository.save(user);
    }
}
