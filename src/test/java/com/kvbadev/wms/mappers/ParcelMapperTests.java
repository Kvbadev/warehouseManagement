package com.kvbadev.wms.mappers;

import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelDto;
import com.kvbadev.wms.presentation.dataTransferObjects.ParcelPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.ParcelMapper;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class ParcelMapperTests {
    private final ParcelMapper parcelMapper = ParcelMapper.INSTANCE;
    @Test
    public void updateShouldUpdateSetValuesAndIgnoreNullValues() {
        Parcel parcel = new Parcel("flsdjf", 300);
        parcel.setId(3);
        Parcel parcelUpdate = new Parcel("test2", 4);

        parcelMapper.update(parcel, parcelUpdate);

        assert parcel.getId() == 3;
        assert Objects.equals(parcel.getName(), parcelUpdate.getName());
    }
    @Test
    public void parcelDtoToParcelShouldIgnoreDeliveryAndShelf() {
        ParcelDto parcelUpdate = new ParcelDto("test2", 2, 3, 33);
        Parcel parcel = parcelMapper.parcelDtoToParcel(parcelUpdate);

        assert parcel.getWeight() == 2;
        assert parcel.getId() == null;
        assert parcel.getDelivery() == null;
        assert parcel.getShelf() == null;
    }

    @Test
    public void parcelPutToParcelShouldIgnoreShelfAndUpdateId() {
        ParcelPutRequest parcelUpdate = new ParcelPutRequest(1, "hello",4, null,4);
        Parcel parcel = parcelMapper.parcelPutToParcel(parcelUpdate);

        assert parcel.getWeight() == 4;
        assert parcel.getId() == 1;
        assert parcel.getShelf() == null;
    }
}
