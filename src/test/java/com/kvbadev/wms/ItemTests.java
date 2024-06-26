package com.kvbadev.wms;

import com.kvbadev.wms.models.warehouse.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Objects;


@ExtendWith(SpringExtension.class)
public class ItemTests {

    @Test
    public void checkIf_itemNetPrice_is_convertedCorrectly() {
        Item item = new Item("name", "description",3, 33125L);
        System.out.println(item.getNormalizedNetPrice());
        assert Objects.equals(item.getNormalizedNetPrice(), new BigDecimal("331.25"));
    }
}
