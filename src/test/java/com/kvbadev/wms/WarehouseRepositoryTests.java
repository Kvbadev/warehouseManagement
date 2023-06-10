package com.kvbadev.wms;

import com.kvbadev.wms.data.WarehouseRepository;
import com.kvbadev.wms.models.warehouse.StorageRack;
import com.kvbadev.wms.models.warehouse.Warehouse;
import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.instancio.Select.field;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WarehouseRepositoryTests {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @Test
    @Order(1)
    @Transactional
    public void saveWarehouseWithChildren() {
        var warehouseRacks = Instancio.ofList(StorageRack.class).size(10)
                .ignore(field(StorageRack::getWarehouse))
                .ignore(field(StorageRack::getShelves))
                .ignore(field(StorageRack::getRoutes))
                .create();

        var warehouse = new Warehouse();
        for(var rack : warehouseRacks) {
            warehouse.addStorageRack(rack);
        }

        warehouseRepository.save(warehouse);

        var whFromDb = warehouseRepository.findById(1L);
        assert whFromDb.isPresent();
        assert whFromDb.get().rackList().size() == 10;
        assert whFromDb.get().rackList().get(0).getWarehouse().equals(whFromDb.get());
    }
}
