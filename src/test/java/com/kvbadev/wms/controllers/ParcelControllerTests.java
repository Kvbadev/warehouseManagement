package com.kvbadev.wms.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.data.warehouse.ShelfRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.controllers.ParcelsController;
import com.kvbadev.wms.presentation.modelAssemblers.ParcelModelAssembler;
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

@WebMvcTest(value = ParcelsController.class, excludeAutoConfiguration = {ParcelsController.class})
@AutoConfigureMockMvc(addFilters = false)
public class ParcelControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ParcelModelAssembler parcelModelAssembler;
    @MockBean
    private ParcelRepository parcelRepository;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private ShelfRepository shelfRepository;
    @MockBean
    private ItemRepository itemRepository;
    private final ObjectMapper objectMapper;

    public ParcelControllerTests(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    void findAllParcelsShouldReturnCollectionModelOfParcelEntityModels() throws Exception {
        List<Parcel> parcels = new ArrayList<>(
                List.of(
                        new Parcel("name1", 4000),
                        new Parcel("name2", 300)
                )
        );
        when(parcelRepository.findAll()).thenReturn(parcels);
        when(parcelModelAssembler.toModel(any(Parcel.class)))
                .thenReturn(EntityModel.of(parcels.get(0)));

        this.mockMvc.perform(get("/parcels"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.parcelList.[0].name").value("name1"));

    }

    @Test
    void findOneParcelShouldReturnParcelEntityModel() throws Exception {
        Parcel parcel = new Parcel("name1", 432);
        parcel.setId(2);

        when(parcelRepository.findById(2)).thenReturn(Optional.of(parcel));
        when(parcelModelAssembler.toModel(parcel)).thenReturn(EntityModel.of(parcel));

        this.mockMvc.perform(get("/parcels/2"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.weight").value(432));
    }

    @Test
    void findOneParcelInvalidIdShouldReturn404AndMessage() throws Exception {
        when(parcelRepository.findById(2)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/parcels/2"))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Could not find Parcel with id: 2"));
    }

    @Test
    void createParcelWithCorrectBodyReturnsHttpCreated() throws Exception {
        Parcel parcel = new Parcel("name1", 432);

        when(parcelRepository.save(any())).thenAnswer(invocation -> {
            parcel.setId(1);
            return parcel;
        });

        when(parcelModelAssembler.toModel(parcel)).thenReturn(EntityModel.of(parcel));

        this.mockMvc.perform(post("/parcels")
                        .content(objectMapper.writeValueAsString(parcel))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(header().stringValues("Location", "http://localhost/parcels/1")); //no api context path because it's a test
    }

    @Test
    void putParcelWithExistingIdReturnsHttpOkAndLocationIsNotSet() throws Exception {
        Parcel putRequest = new Parcel("name2", 432);
        Parcel parcel = new Parcel("name", 500);
        putRequest.setId(1);
        parcel.setId(1);

        objectMapper.updateValue(parcel, putRequest);

        when(parcelRepository.save(any())).thenReturn(parcel);
        when(parcelRepository.findById(1)).thenReturn(Optional.of(parcel));

        when(parcelModelAssembler.toModel(parcel)).thenReturn(EntityModel.of(parcel));

        this.mockMvc.perform(put("/parcels")
                        .content(objectMapper.writeValueAsString(putRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("name2"))
                .andExpect(header().doesNotExist("Location")); //no api context path because it's a test
    }

    @Test
    void putParcelWithoutIdReturnsHttpCreatedAndLocationIsSet() throws Exception {
        Parcel parcel = new Parcel("name22", 432);

        when(parcelRepository.save(any())).thenAnswer(invocation -> {
            parcel.setId(1);
            return parcel;
        });
        when(parcelRepository.findById(1)).thenReturn(Optional.of(parcel));

        when(parcelModelAssembler.toModel(parcel)).thenReturn(EntityModel.of(parcel));

        this.mockMvc.perform(put("/parcels")
                        .content(objectMapper.writeValueAsString(parcel))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(parcel.getName()))
                .andExpect(header().stringValues("Location", "http://localhost/parcels/1")); //no api context path because it's a test
    }

    @Test
    void patchParcelReturnsHttpOk() throws Exception {
        Parcel patchRequests = new Parcel();
        patchRequests.setWeight(520);
        Parcel parcel = new Parcel("name", 432);

        when(parcelRepository.save(any())).thenAnswer(invocation -> {
            objectMapper.updateValue(parcel, patchRequests);
            return parcel;
        });
        when(parcelRepository.findById(1)).thenReturn(Optional.of(parcel));

        when(parcelModelAssembler.toModel(parcel)).thenReturn(EntityModel.of(parcel));

        this.mockMvc.perform(patch("/parcels/1")
                        .content(objectMapper.writeValueAsString(parcel))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(520));
    }
    @Test
    void deleteParcelReturnsNoContent() throws Exception {
        this.mockMvc.perform(delete("/parcels/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteParcelIncorrectIdReturns404() throws Exception {
        doThrow(new EntityNotFoundException(Parcel.class, 233)).when(parcelRepository).deleteById(any());
        this.mockMvc.perform(delete("/parcels/223"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getParentParcelsReturnsOk() throws Exception {
        Parcel parcel = new Parcel("test", 23);
        Item item = new Item("i1","",2, 3000L);

        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(parcelRepository.findByItemId(anyInt())).thenReturn(Optional.of(parcel));
        when(parcelModelAssembler.toModel(any(Parcel.class)))
                .thenReturn(EntityModel.of(parcel));

        this.mockMvc.perform(get("/parcels").param("itemId", "1"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.name").value("test"));
    }
}
