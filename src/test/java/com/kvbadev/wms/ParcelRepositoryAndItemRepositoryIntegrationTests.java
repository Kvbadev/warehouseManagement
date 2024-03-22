package com.kvbadev.wms;

import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@SpringBootTest
@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ParcelRepositoryAndItemRepositoryIntegrationTests {
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void saveParcelWithItems_thenCheck_ifItemsAreSaved() {
        Item p1 = new Item("Test1", "",2, 4450L);
        Item p2 = new Item("Test2", "",3, 3381L);

        Parcel parcel = new Parcel("name", 3000);

        p1.setParcel(parcel);
        p2.setParcel(parcel);
        parcelRepository.save(parcel);

        assert itemRepository.count() == 0;

        itemRepository.save(p1);
        itemRepository.save(p2);

        assert itemRepository.count() == 2;
    }
}
