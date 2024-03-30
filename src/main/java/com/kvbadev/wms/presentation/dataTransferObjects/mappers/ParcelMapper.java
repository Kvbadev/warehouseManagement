package com.kvbadev.wms.presentation.dataTransferObjects.mappers;

import com.kvbadev.wms.models.security.User;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelDto;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelPutRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParcelMapper {
    ParcelMapper INSTANCE = Mappers.getMapper(ParcelMapper.class);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Parcel parcel, Parcel updateParcel);
    @Mapping(target = "shelf", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    @Mapping(target = "id", ignore = true)
    Parcel parcelDtoToParcel(ParcelDto parcelDto);

    @Mapping(target = "shelf", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    Parcel parcelPutToParcel(ParcelPutRequest parcel);
}

