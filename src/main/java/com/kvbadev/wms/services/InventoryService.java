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
    public void removeItem(Item item) {
        itemRepository.delete(item);
    }

    /**
     * Gets all the items contained in a parcel with provided id
     * @param parcelId an id of the desired parcel
     * @return items included in the parcel with the provided id
     */
    public List<Item> findAllParcelItems(int parcelId) {
        return itemRepository.findAllParcelItems(parcelId);
    }

    /**
     * Gets the item's parental parcel using the provided id
     * @param itemId an id of the desired item
     * @return a parcel in which the item is contained
     * @see Parcel
     */
    public Parcel findParentParcel(int itemId) {
        return parcelRepository.findByItemId(itemId).orElse(null);
    }

    public Item saveItem(Item item) {
        //validate item first!
        return itemRepository.save(item);
    }

    public List<Parcel> findAllParcels() {
        return parcelRepository.findAll();
    }

    public Parcel findParcelById(int id) {
        return parcelRepository.findById(id).orElse(null);
    }

    public Parcel saveParcel(Parcel parcel) {
        //validate the parcel
        return parcelRepository.save(parcel);
    }
}
