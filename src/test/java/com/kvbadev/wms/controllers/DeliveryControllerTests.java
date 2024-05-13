package com.kvbadev.wms.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.data.warehouse.DeliveryRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.data.warehouse.ShelfRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.presentation.controllers.DeliveriesController;
import com.kvbadev.wms.presentation.modelAssemblers.DeliveryModelAssembler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = DeliveriesController.class, excludeAutoConfiguration = {DeliveriesController.class})
@AutoConfigureMockMvc(addFilters = false)
public class DeliveryControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeliveryModelAssembler DeliveryModelAssembler;
    @MockBean
    private DeliveryRepository deliveryRepository;
    @MockBean
    private ShelfRepository shelfRepository;
    @MockBean
    private ParcelRepository parcelRepository;
    private final ObjectMapper objectMapper;
    private final LocalDate currentDate = LocalDate.now();

    public DeliveryControllerTests(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).findAndRegisterModules();
        System.out.println(currentDate);
    }

    @Test
    void findAllDeliveriesShouldReturnCollectionModelOfDeliveryEntityModels() throws Exception {
        List<Delivery> Deliveries = new ArrayList<>(
                List.of(
                        new Delivery(currentDate, false)
                )
        );
        when(deliveryRepository.findAll()).thenReturn(Deliveries);
        when(DeliveryModelAssembler.toModel(any(Delivery.class)))
                .thenReturn(EntityModel.of(Deliveries.get(0)));

        this.mockMvc.perform(get("/api/deliveries"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.deliveryList.[0].arrivalDate").value(currentDate.toString()));

    }

    @Test
    void findOneDeliveryShouldReturnDeliveryEntityModel() throws Exception {
        Delivery Delivery = new Delivery(currentDate, false);
        Delivery.setId(2);

        when(deliveryRepository.findById(2)).thenReturn(Optional.of(Delivery));
        when(DeliveryModelAssembler.toModel(Delivery)).thenReturn(EntityModel.of(Delivery));

        this.mockMvc.perform(get("/api/deliveries/2"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.arrivalDate").value(currentDate.toString()));
    }

    @Test
    void findOneDeliveryInvalidIdShouldReturn404AndMessage() throws Exception {
        when(deliveryRepository.findById(2)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/deliveries/2"))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Could not find Delivery with id: 2"));
    }

    @Test
    void createDeliveryWithCorrectBodyReturnsHttpCreated() throws Exception {
        Delivery delivery = new Delivery(currentDate, false);

        when(deliveryRepository.save(any())).thenAnswer(invocation -> {
            delivery.setId(1);
            return delivery;
        });

        when(DeliveryModelAssembler.toModel(delivery)).thenReturn(EntityModel.of(delivery));
        var x = objectMapper.writeValueAsString(delivery);

        this.mockMvc.perform(post("/api/deliveries")
                        .content(objectMapper.writeValueAsString(delivery))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content()
                        .contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(header().stringValues("Location", "http://localhost/api/deliveries/1")); //no api context path because it's a test
    }

    @Test
    void putDeliveryWithExistingIdReturnsHttpOkAndLocationIsNotSet() throws Exception {
        LocalDate newCurrent = LocalDate.now().plusDays(2);
        Delivery putRequest = new Delivery(newCurrent, false);
        Delivery delivery = new Delivery(currentDate, false);
        putRequest.setId(1);
        delivery.setId(1);

        objectMapper.updateValue(delivery, putRequest);

        when(deliveryRepository.save(any())).thenReturn(delivery);
        when(deliveryRepository.findById(1)).thenReturn(Optional.of(delivery));

        when(DeliveryModelAssembler.toModel(delivery)).thenReturn(EntityModel.of(delivery));

        this.mockMvc.perform(put("/api/deliveries")
                        .content(objectMapper.writeValueAsString(putRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrivalDate").value(newCurrent.toString()))
                .andExpect(header().doesNotExist("Location")); //no api context path because it's a test
    }

    @Test
    void putDeliveryWithoutIdReturnsHttpCreatedAndLocationIsSet() throws Exception {
        Delivery Delivery = new Delivery(currentDate, false);

        when(deliveryRepository.save(any())).thenAnswer(invocation -> {
            Delivery.setId(1);
            return Delivery;
        });
        when(deliveryRepository.findById(1)).thenReturn(Optional.of(Delivery));

        when(DeliveryModelAssembler.toModel(Delivery)).thenReturn(EntityModel.of(Delivery));

        this.mockMvc.perform(put("/api/deliveries")
                        .content(objectMapper.writeValueAsString(Delivery))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.arrivalDate").value(currentDate.toString()))
                .andExpect(header().stringValues("Location", "http://localhost/api/deliveries/1")); //no api context path because it's a test
    }

    @Test
    void patchDeliveryReturnsHttpOk() throws Exception {
        LocalDate newCurrent = LocalDate.now().plusDays(2);
        Delivery patchRequests = new Delivery(newCurrent, false);
        Delivery delivery = new Delivery(currentDate, false);

        when(deliveryRepository.save(any())).thenAnswer(invocation -> {
            objectMapper.updateValue(delivery, patchRequests);
            return delivery;
        });
        when(deliveryRepository.findById(1)).thenReturn(Optional.of(delivery));

        when(DeliveryModelAssembler.toModel(delivery)).thenReturn(EntityModel.of(delivery));

        this.mockMvc.perform(patch("/api/deliveries/1")
                        .content(objectMapper.writeValueAsString(delivery))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrivalDate").value(newCurrent.toString()));

    }
    @Test
    void deleteDeliveryReturnsNoContent() throws Exception {
        this.mockMvc.perform(delete("/api/deliveries/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteDeliveryIncorrectIdReturns404() throws Exception {
        doThrow(new EntityNotFoundException(Delivery.class, 233)).when(deliveryRepository).deleteById(any());
        this.mockMvc.perform(delete("/api/deliveries/223"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByParcelIdReturnsOk() throws Exception {
        Delivery delivery = new Delivery(currentDate, false);

        when(deliveryRepository.findByParcelId(1)).thenReturn(Optional.of(delivery));
        when(DeliveryModelAssembler.toModel(delivery)).thenReturn(EntityModel.of(delivery));

        this.mockMvc.perform(get("/api/deliveries?parcelId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.arrivalDate").value(currentDate.toString()));
    }

}
