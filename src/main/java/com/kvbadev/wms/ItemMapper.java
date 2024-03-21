package com.kvbadev.wms;

import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);
    @Mapping(source = "parcel.id", target = "parcelId")
    ItemDto itemToItemDto(Item item);
    @Mapping(target = "parcel", ignore = true)
    Item itemDtoToItem(ItemDto itemDto);
    Item mapItem(Item item);
}
