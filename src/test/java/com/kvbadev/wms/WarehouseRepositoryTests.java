package com.kvbadev.wms;

import com.kvbadev.wms.data.WarehouseRepository;
import com.kvbadev.wms.models.warehouse.StorageRack;
import com.kvbadev.wms.models.warehouse.Warehouse;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WarehouseRepositoryTests {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    @Order(1)
    public void saveWarehouseWithChildren() {
        var rack1 = new StorageRack();
        var rack2 = new StorageRack();
        var storageRacks = Arrays.asList();

        Warehouse warehouse = new Warehouse();
    }
}
