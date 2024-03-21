package com.kvbadev.wms;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.models.warehouse.Delivery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;

@ExtendWith(SpringExtension.class)
public class ObjectMapperTests {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void mapEntityToPartialEntity() throws JsonMappingException {
        Date now = new Date(System.currentTimeMillis());
        Delivery delivery = new Delivery(now);
        delivery.setId(1);

        DeliveryDto deliveryDto = new DeliveryDto(Date.valueOf(now.toLocalDate().plusDays(4)));
        objectMapper.updateValue(delivery, deliveryDto);

        assert delivery.getArrivalDate().toLocalDate().equals(now.toLocalDate().plusDays(4));
        assert delivery.getId() == 1;
    }
}
