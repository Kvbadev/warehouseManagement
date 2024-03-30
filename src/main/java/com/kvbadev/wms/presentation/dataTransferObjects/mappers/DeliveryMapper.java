package com.kvbadev.wms.presentation.dataTransferObjects.mappers;

import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryPutRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Delivery delivery, Delivery updateDelivery);
    Delivery deliveryDtoToDelivery(DeliveryDto deliveryDto);

    Delivery deliveryPutToDelivery(DeliveryPutRequest parcel);
}
