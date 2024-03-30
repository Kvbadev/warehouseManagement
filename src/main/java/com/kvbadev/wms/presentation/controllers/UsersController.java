package com.kvbadev.wms.presentation.controllers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.dataTransferObjects.UserDto;
import com.kvbadev.wms.presentation.dataTransferObjects.UserPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.UserView;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.UserMapper;
import com.kvbadev.wms.presentation.modelAssemblers.UserViewModelAssembler;
import com.kvbadev.wms.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UsersController extends BaseController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserViewModelAssembler userViewModelAssembler;
    @Autowired
    private UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserView>>> getUsers() {
        List<EntityModel<UserView>> users = userRepository.findAll()
                .stream()
                .map(userMapper::userToUserView)
                .map(userViewModelAssembler::toModel)
                .toList();
        return ResponseEntity.ok(
                CollectionModel.of(users)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<UserView>> getUser(@PathVariable("id") int id) {
        return ResponseEntity.ok(
                userRepository
                        .findById(id)
                        .map(userMapper::userToUserView)
                        .map(userViewModelAssembler::toModel)
                        .orElseThrow(() -> new EntityNotFoundException(User.class, id))
        );
    }

    @PostMapping(produces = HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UserView>> createUser(@Valid @RequestBody UserDto userDto) {
        User newUser = userService.saveUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, buildLocationHeader(newUser.getId()))
                .body(userViewModelAssembler.toModel(userMapper.userToUserView(newUser)));
    }

    @PutMapping(produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UserView>> putUser(@Valid @RequestBody UserPutRequest userPutRequest) {
        User user = userService.updateUser(userPutRequest);

        HttpStatus responseStatus = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();

        if (!Objects.equals(user.getId(), userPutRequest.getId())) { //if id has changed - new resource was created
            responseStatus = HttpStatus.CREATED;
            headers.add(HttpHeaders.LOCATION, buildLocationHeader(user.getId()));
        }

        return ResponseEntity
                .status(responseStatus)
                .headers(headers)
                .body(userViewModelAssembler.toModel(userMapper.userToUserView(user)));
    }

    @PatchMapping(value = "{Id}", produces = HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<UserView>> patchUser(
            @PathVariable("Id") int id,
            @RequestBody UserDto userUpdateRequest
    ) {

        User user = userService.updateUser(userUpdateRequest, id);
        userRepository.save(user);

        return ResponseEntity.ok(userViewModelAssembler.toModel(userMapper.userToUserView(user)));
    }

    @DeleteMapping(value = "{Id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable("Id") int Id) {
        try {
            userRepository.deleteById(Id);
            return ResponseEntity.noContent().build();

        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(User.class, Id);
        }
    }

}
