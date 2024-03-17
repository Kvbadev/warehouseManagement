package com.kvbadev.wms;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kvbadev.wms.controllers.dtos.DeliveryDetail;
import com.kvbadev.wms.models.warehouse.Delivery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ObjectMapperTests {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void mapEntityToPartialEntity() throws JsonMappingException {
        Date now = new Date(System.currentTimeMillis());
        Delivery delivery = new Delivery(now);
        delivery.setId(1);

        DeliveryDetail deliveryDetail = new DeliveryDetail(Date.valueOf(now.toLocalDate().plusDays(4)));
        objectMapper.updateValue(delivery, deliveryDetail);

        assert delivery.getArrivalDate().toLocalDate().equals(now.toLocalDate().plusDays(4));
        assert delivery.getId() == 1;
    }
}
