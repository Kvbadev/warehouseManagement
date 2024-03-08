package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("byRole/{role}")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userRepository.findByRole(role);
    }

    @GetMapping("{Id}")
    public User getUserById(@PathVariable("Id") int Id) {
        Optional<User> user = userRepository.findById(Id);
        return user.orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createUser(@RequestBody User user) {
        userRepository.save(user);
    }
}
