package com.kvbadev.wms.presentation.dataTransferObjects.mappers;

import com.kvbadev.wms.models.warehouse.Delivery;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryDto;
import com.kvbadev.wms.presentation.dataTransferObjects.DeliveryPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    @Mapping(target = "id", ignore = true)
    Delivery deliveryDtoToDelivery(DeliveryDto deliveryDto);

    Delivery deliveryPutToDelivery(DeliveryPutRequest parcel);
}
