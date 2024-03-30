package com.kvbadev.wms.mappers;

import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemDto;
import com.kvbadev.wms.presentation.dataTransferObjects.ItemPutRequest;
import com.kvbadev.wms.presentation.dataTransferObjects.mappers.ItemMapper;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class ItemMapperTests {
    private final ItemMapper itemMapper = ItemMapper.INSTANCE;
    @Test
    public void updateShouldUpdateSetValuesAndIgnoreNullValues() {
        Item item = new Item("flsdjf", "", null, 330L);
        Item itemUpdate = new Item("test2", "", 3, 330L);

        itemMapper.update(item, itemUpdate);

        assert item.getQuantity() == 3;
        assert Objects.equals(item.getName(), itemUpdate.getName());
    }
    @Test
    public void itemDtoToItemShouldIgnoreParcelAndId() {
        ItemDto itemUpdate = new ItemDto("test2", "", 3, 330L, 2);
        Item item = itemMapper.itemDtoToItem(itemUpdate);

        assert item.getQuantity() == 3;
        assert item.getId() == null;
        assert item.getParcel() == null;
    }

    @Test
    public void itemPutToItemShouldIgnoreParcelAndUpdateId() {
        ItemPutRequest itemUpdate = new ItemPutRequest(1, "hello","",4,4400L,2);
        Item item = itemMapper.itemPutToItem(itemUpdate);

        assert item.getQuantity() == 4;
        assert item.getId() == 1;
        assert item.getParcel() == null;
    }
}
