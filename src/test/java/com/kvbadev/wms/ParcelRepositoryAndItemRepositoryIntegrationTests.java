package com.kvbadev.wms;

import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.data.warehouse.ParcelRepository;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.Parcel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@SpringBootTest
@ActiveProfiles("test")
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

        assert itemRepository.count() == 0;

        itemRepository.save(p1);
        itemRepository.save(p2);

        assert itemRepository.count() == 2;
    }
}
