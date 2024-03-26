package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.security.RoleRepository;
import com.kvbadev.wms.models.security.Role;
import com.kvbadev.wms.presentation.dataTransferObjects.UserPutRequest;
import com.kvbadev.wms.services.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.dataTransferObjects.UserDto;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.UserMapper;
import com.kvbadev.wms.presentation.modelAssemblers.UserModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserModelAssembler userModelAssembler;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    private final UserMapper userMapper;

    public UsersController() {
        userMapper = UserMapper.INSTANCE;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers() {
        List<EntityModel<User>> users = userRepository.findAll()
                .stream()
                .map(userModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(users)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable("id") int id) {
        return ResponseEntity.ok(
                userRepository
                        .findById(id)
                        .map(userModelAssembler::toModel)
                        .orElseThrow(() -> new EntityNotFoundException(User.class, "id", id))
        );
    }

    @PostMapping(produces = HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody UserDto userDto) {
        User user = userMapper.userDtoToUser(userDto);
        //add roles if any exist
        if (userDto.getRolesId() != null) {
            userService.addRolesByRolesId(user, userDto.getRolesId());
        }
        user = userRepository.save(user);
        //TODO: registration and login

        String userLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUriString();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, userLocation)
                .body(userModelAssembler.toModel(user));
    }

    @PutMapping(produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<User>> putUser(@RequestBody UserPutRequest userPutRequest) {
        HttpStatus responseStatus = HttpStatus.CREATED;

        if (userPutRequest.getId() != null) {
            //if the resource already exists, change response to OK from CREATED
            responseStatus = HttpStatus.OK;
            userRepository.findById(userPutRequest.getId())
                    .orElseThrow(() -> new EntityNotFoundException(User.class, userPutRequest.getId()));
        }

        User newUser = userMapper.userPutToUser(userPutRequest);
        //add roles if any exist
        if (userPutRequest.getRolesId() != null) {
            userService.addRolesByRolesId(newUser, userPutRequest.getRolesId());
        }

        newUser = userRepository.save(newUser);
        HttpHeaders headers = new HttpHeaders();

        if (responseStatus == HttpStatus.CREATED) {
            String userLocation = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newUser.getId())
                    .toUriString();
            headers.add(HttpHeaders.LOCATION, userLocation);
        }

        return ResponseEntity
                .status(responseStatus)
                .headers(headers)
                .body(userModelAssembler.toModel(newUser));
    }

    @PatchMapping(value = "{Id}", produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<User>> patchUser(
            @PathVariable("Id") int id,
            @RequestBody UserDto userUpdateRequest
    ) throws JsonMappingException {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, id));

        //update shallow values
        User userRequest = userMapper.userDtoToUser(userUpdateRequest);
        objectMapper.updateValue(user, userRequest);

        //update roles
        if (userUpdateRequest.getRolesId() != null) {
            userService.addRolesByRolesId(user, userUpdateRequest.getRolesId());
        }

        userRepository.save(user);
        return ResponseEntity.ok(userModelAssembler.toModel(user));
    }

    @DeleteMapping(value = "{Id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable("Id") int Id) {
        try {
            userRepository.deleteById(Id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(User.class, "id", Id);
        }
    }

}
