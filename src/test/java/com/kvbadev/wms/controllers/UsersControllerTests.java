package com.kvbadev.wms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.exceptions.DuplicateResourceException;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.controllers.UsersController;
import com.kvbadev.wms.presentation.modelAssemblers.UserViewModelAssembler;
import com.kvbadev.wms.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UsersController.class, excludeAutoConfiguration = {UsersController.class})
@AutoConfigureMockMvc(addFilters = false)
@Import(UserViewModelAssembler.class)
public class UsersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserViewModelAssembler userViewModelAssembler;
    private final User testUser = new User("fffff", "lllll", "mail@mail.com", "Pa$$20dkls..3");

    @Test
    void findAllDeliveriesShouldReturnCollectionModelOfUserEntityModels() throws Exception {
        List<User> users = new ArrayList<>(
                List.of(
                        testUser
                )
        );
        when(userRepository.findAll()).thenReturn(users);
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.userList.[0].firstName").value(testUser.getFirstName()));

    }

    @Test
    void findOneUserShouldReturnUserEntityModel() throws Exception {
        when(userRepository.findById(2)).thenReturn(Optional.of(testUser));

        this.mockMvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.lastName").value(testUser.getLastName()));
    }

    @Test
    void findOneUserNonExistentIdShouldReturn404AndMessage() throws Exception {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/users/2"))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Could not find User with id: 2"));
    }

    @Test
    void createUserWithCorrectBodyReturnsHttpCreated() throws Exception {
        User user = new User(testUser);
        user.setId(1);
        when(userRepository.save(any())).thenReturn(user);
        when(userService.saveUser(any())).thenReturn(user);


        this.mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(header().stringValues("Location", "http://localhost/users/1")); //no api context path because it's a test
    }

    @Test
    void createUserWithSameEmailThrowsDuplicateException() throws Exception {
        when(userService.saveUser(any())).thenThrow(new DuplicateResourceException(User.class, "email", testUser.getEmail()));
        this.mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void putUserWithExistingIdReturnsHttpOkAndLocationIsNotSet() throws Exception {
        User putRequest = new User("alsjdfka", "lfaksdl", "mail@mai2.com", "P@#safdasdfk003@.");
        User user = new User(testUser);
        putRequest.setId(1);
        user.setId(1);
        when(userService.updateUser(any())).thenReturn(user);

        objectMapper.updateValue(user, putRequest);

        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));


        this.mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(putRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("alsjdfka"))
                .andExpect(header().doesNotExist("Location"));
    }

    @Test
    void putUserWithoutIdReturnsHttpCreatedAndLocationIsSet() throws Exception {
        User user = new User(testUser);

        when(userService.updateUser(any())).thenAnswer(invocation -> {
            user.setId(1);
            return user;
        });
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));


        this.mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(header().stringValues("Location", "http://localhost/users/1")); //no api context path because it's a test
    }

    @Test
    void patchUserReturnsHttpOk() throws Exception {
        User patchRequests = new User(testUser);
        patchRequests.setLastName("imminnea");
        User u = new User(testUser);
        u.setId(1);

        when(userRepository.save(any())).thenAnswer(invocation -> {
            objectMapper.updateValue(u, patchRequests);
            return u;
        });
        when(userService.updateUser(any(), anyInt())).thenReturn(u);


        this.mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(u))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value(u.getLastName()));

    }

    @Test
    void deleteUserReturnsNoContent() throws Exception {
        this.mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserIncorrectIdReturns404() throws Exception {
        doThrow(new EntityNotFoundException(User.class, 233)).when(userRepository).deleteById(any());
        this.mockMvc.perform(delete("/users/223"))
                .andExpect(status().isNotFound());
    }

}
