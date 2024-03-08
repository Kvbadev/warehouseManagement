package com.kvbadev.wms;

import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.models.warehouse.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.logging.Logger;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ItemRepositoryTests {

    private static Logger log = Logger.getLogger(ItemRepositoryTests.class.getName());
    @Autowired
    private ItemRepository itemRepository;

    Item testItem = new Item("Test1", "test2", 4450);

    @Test
    @DisplayName("Check if an item is accessible and have correct data after adding to the itemRepository")
    @Order(1)
    public void saveAndExamineItem() {
        itemRepository.save(testItem);
        var res = itemRepository.findAll();

        assert res.size() > 0;
        assert res.get(0) != null;
        assert Objects.equals(res.get(0).getNormalizedNetPrice(), testItem.getNormalizedNetPrice());
    }

    @Test
    @DisplayName("Check if an item is being removed from the itemRepository")
    @Order(2)
    public void removeItem() {
        //clear the repository
        itemRepository.deleteAll();

        itemRepository.save(testItem);
        var product = itemRepository.findAll();

        itemRepository.delete(product.get(0));
        assert itemRepository.findAll().isEmpty();
    }
}
