package com.kvbadev.wms.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.controllers.ItemsController;
import com.kvbadev.wms.presentation.modelAssemblers.ItemModelAssembler;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ItemsController.class, excludeAutoConfiguration = {ItemsController.class})
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private ItemModelAssembler itemModelAssembler;
    @MockBean
    private ParcelRepository parcelRepository;
    private final ObjectMapper objectMapper;

    public ItemControllerTest(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    void findAllShouldReturnCollectionModelOfItemEntityModels() throws Exception {
        List<Item> items = new ArrayList<>(
                List.of(
                        new Item("name1", "", 4, 444L),
                        new Item("name2", "", 3, 4L)
                )
        );
        when(itemRepository.findAll()).thenReturn(items);
        when(itemModelAssembler.toModel(any(Item.class)))
                .thenReturn(EntityModel.of(items.get(0)));

        this.mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.itemList.[0].name").value("name1"));

    }

    @Test
    void findOneShouldReturnItemEntityModel() throws Exception {
        Item item = new Item("name1", "test", 4, 432L);
        item.setId(2);

        when(itemRepository.findById(2)).thenReturn(Optional.of(item));
        when(itemModelAssembler.toModel(item)).thenReturn(EntityModel.of(item));

        this.mockMvc.perform(get("/items/2"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void findOneInvalidIdShouldReturn404AndMessage() throws Exception {
        when(itemRepository.findById(2)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/items/2"))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Could not find Item with id: 2"));
    }

    @Test
    void createItemWithCorrectBodyReturnsHttpCreated() throws Exception {
        Item item = new Item("name1", "test", 4, 432L);

        when(itemRepository.save(any())).thenAnswer(invocation -> {
            item.setId(1);
            return item;
        });

        when(itemModelAssembler.toModel(item)).thenReturn(EntityModel.of(item));

        this.mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(header().stringValues("Location", "http://localhost/items/1")); //no api context path because it's a test
    }

    @Test
    void putItemWithExistingIdReturnsHttpOkAndLocationIsNotSet() throws Exception {
        Item putRequest = new Item("name2", "test2", 4, 432L);
        Item item = new Item("name", "test", 4, 432L);
        putRequest.setId(1);
        item.setId(1);

        objectMapper.updateValue(item, putRequest);

        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        when(itemModelAssembler.toModel(item)).thenReturn(EntityModel.of(item));

        this.mockMvc.perform(put("/items")
                        .content(objectMapper.writeValueAsString(putRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name2"))
                .andExpect(header().doesNotExist("Location")); //no api context path because it's a test
    }

    @Test
    void putItemWithoutIdReturnsHttpCreatedAndLocationIsSet() throws Exception {
        Item item = new Item("name", "test", 4, 432L);

        when(itemRepository.save(any())).thenAnswer(invocation -> {
            item.setId(1);
            return item;
        });
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        when(itemModelAssembler.toModel(item)).thenReturn(EntityModel.of(item));

        this.mockMvc.perform(put("/items")
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(header().stringValues("Location", "http://localhost/items/1")); //no api context path because it's a test
    }

    @Test
    void patchItemReturnsHttpOk() throws Exception {
        Item patchRequests = new Item();
        patchRequests.setDescription("Hello World");
        Item item = new Item("name", "test", 4, 432L);

        when(itemRepository.save(any())).thenAnswer(invocation -> {
            objectMapper.updateValue(item, patchRequests);
            return item;
        });
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        when(itemModelAssembler.toModel(item)).thenReturn(EntityModel.of(item));

        this.mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Hello World"));
    }
    @Test
    void deleteItemReturnsNoContent() throws Exception {
        Item item = new Item("name", "test", 4, 432L);

        this.mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteItemIncorrectIdReturns404() throws Exception {
        doThrow(new EntityNotFoundException(Item.class, 233)).when(itemRepository).deleteById(any());
        this.mockMvc.perform(delete("/items/223"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getParcelItemsReturnsOk() throws Exception {
        List<Item> items = new ArrayList<>(
                List.of(
                        new Item("name1", "", 4, 444L),
                        new Item("name2", "", 3, 4L)
                )
        );
        Parcel parcel = new Parcel("p1", 3000);
        items.forEach(i -> i.setParcel(parcel));

        when(parcelRepository.findById(any())).thenReturn(Optional.of(parcel));
        when(itemRepository.findItemsByParcelId(anyInt())).thenReturn(items);
        when(itemModelAssembler.toModel(any(Item.class)))
                .thenReturn(EntityModel.of(items.get(0)));

        this.mockMvc.perform(get("/items").param("parcelId", "1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.itemList.[0].name").value("name1"));
    }
}
