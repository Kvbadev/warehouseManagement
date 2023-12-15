package com.kvbadev.wms.services;

import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ParcelRepository parcelRepository;

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Item findItemById(int id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<Item> findAllParcelItems(int parcelId) {
        return itemRepository.findAllParcelItems(parcelId);
    }

    public Parcel findParentalParcel(int itemId) {
        return parcelRepository.findByItemId(itemId).orElse(null);
    }

    public void saveItem(Item item) {
        //validate item first!
        itemRepository.save(item);
    }

    public List<Parcel> findAllParcels() {
        return parcelRepository.findAll();
    }

    public Parcel findParcelById(int id) {
        return parcelRepository.findById(id).orElse(null);
    }

    public void saveParcel(Parcel parcel) {
        //validate the parcel
        parcelRepository.save(parcel);
    }
}
