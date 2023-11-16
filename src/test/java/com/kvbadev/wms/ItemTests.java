package com.kvbadev.wms;

import com.kvbadev.wms.models.warehouse.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemTests {

    @Test
    public void checkIf_itemNetPrice_is_convertedCorrectly() {
        Item item = new Item("name", "description", 33125);
        System.out.println(item.getNormalizedNetPrice());
        assert Objects.equals(item.getNormalizedNetPrice(), new BigDecimal("331.25"));
    }
}
