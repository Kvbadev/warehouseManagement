package com.kvbadev.wms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.security.UserRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.presentation.controllers.DeliveriesController;
import com.kvbadev.wms.presentation.controllers.UsersController;
import com.kvbadev.wms.presentation.modelAssemblers.UserModelAssembler;
import com.kvbadev.wms.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UsersController.class, excludeAutoConfiguration = {UsersController.class})
@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserModelAssembler UserModelAssembler;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    private final User testUser = new User("fffff", "lllll", "mail@mail.com", "Pa$$20dkls..3");

    @Test
    void findAllDeliveriesShouldReturnCollectionModelOfUserEntityModels() throws Exception {
        List<User> Deliveries = new ArrayList<>(
                List.of(
                        testUser
                )
        );
        when(userRepository.findAll()).thenReturn(Deliveries);
        when(UserModelAssembler.toModel(any(User.class)))
                .thenReturn(EntityModel.of(Deliveries.get(0)));

        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.userList.[0].firstName").value(testUser.getFirstName()));

    }

    @Test
    void findOneUserShouldReturnUserEntityModel() throws Exception {
        when(userRepository.findById(2)).thenReturn(Optional.of(testUser));
        when(UserModelAssembler.toModel(testUser)).thenReturn(EntityModel.of(testUser));

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
        when(userRepository.save(any())).thenAnswer(invocation -> {
            user.setId(1);
            return user;
        });

        when(UserModelAssembler.toModel(user)).thenReturn(EntityModel.of(user));

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
    void putUserWithExistingIdReturnsHttpOkAndLocationIsNotSet() throws Exception {
        User putRequest = new User("alsjdfka", "lfaksdl", "mail@mai2.com", "P@#safdasdfk003@.");
        User user = new User(testUser);
        putRequest.setId(1);
        user.setId(1);

        objectMapper.updateValue(user, putRequest);

        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        when(UserModelAssembler.toModel(user)).thenReturn(EntityModel.of(user));

        this.mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(putRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("alsjdfka"))
                .andExpect(header().doesNotExist("Location"));
    }

    @Test
    void putUserWithoutIdReturnsHttpCreatedAndLocationIsSet() throws Exception {
        User u = new User(testUser);

        when(userRepository.save(any())).thenAnswer(invocation -> {
            u.setId(1);
            return u;
        });
        when(userRepository.findById(1)).thenReturn(Optional.of(u));

        when(UserModelAssembler.toModel(u)).thenReturn(EntityModel.of(u));

        this.mockMvc.perform(put("/users")
                        .content(objectMapper.writeValueAsString(u))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(u.getEmail()))
                .andExpect(header().stringValues("Location", "http://localhost/users/1")); //no api context path because it's a test
    }

    @Test
    void patchUserReturnsHttpOk() throws Exception {
        User patchRequests = new User(testUser);
        patchRequests.setLastName("imminnea");
        User u = new User(testUser);

        when(userRepository.save(any())).thenAnswer(invocation -> {
            objectMapper.updateValue(u, patchRequests);
            return u;
        });
        when(userRepository.findById(1)).thenReturn(Optional.of(u));

        when(UserModelAssembler.toModel(u)).thenReturn(EntityModel.of(u));

        this.mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(u))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("imminnea"));

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
