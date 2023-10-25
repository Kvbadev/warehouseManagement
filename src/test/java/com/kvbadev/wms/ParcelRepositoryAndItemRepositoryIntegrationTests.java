package com.kvbadev.wms;

import com.kvbadev.wms.data.ItemRepository;
import com.kvbadev.wms.data.ParcelRepository;
import com.kvbadev.wms.models.Item;
import com.kvbadev.wms.models.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ParcelRepositoryAndItemRepositoryIntegrationTests {
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void saveParcelWithItems_thenCheck_ifItemsAreSaved() {
        Item p1 = new Item("Test1", "", 4450);
        Item p2 = new Item("Test2", "", 3381);

        Parcel parcel = new Parcel("name", 3000);

        parcel.addItem(p1);
        parcel.addItem(p2);
        parcelRepository.save(parcel);

        var itemsCount = itemRepository.count();

        assert itemsCount == 2;
    }
}
