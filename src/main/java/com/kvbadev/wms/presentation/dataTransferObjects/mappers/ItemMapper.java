package com.kvbadev.wms.presentation.dataTransferObjects.mappers;

import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemDto;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "parcel", ignore = true)
    @Mapping(target = "id", ignore = true)
    Item itemDtoToItem(ItemDto itemDto);

    @Mapping(target = "parcel", ignore = true)
    Item itemPutToItem(ItemPutRequest item);
}
