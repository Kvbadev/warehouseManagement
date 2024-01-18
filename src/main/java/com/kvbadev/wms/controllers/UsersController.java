package com.kvbadev.wms.controllers;

import com.kvbadev.wms.controllers.dto.UserDto;
import com.kvbadev.wms.data.auth.UserRepository;
import com.kvbadev.wms.models.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<UserDto> getUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> list = userList.stream().map(u -> {
            return new UserDto(u.getFirstName(),u.getLastName(),u.getEmail(), u.isEnabled());
        }).collect(Collectors.toList());
        return list;
    }

    @GetMapping("byRole/{role}")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userRepository.findByRole(role);
    }

    @GetMapping("{Id}")
    public User getUserById(@PathVariable int Id) {
        Optional<User> user = userRepository.findById(Id);
        return user.orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createUser(@RequestBody User user) {
        userRepository.save(user);
    }
}
