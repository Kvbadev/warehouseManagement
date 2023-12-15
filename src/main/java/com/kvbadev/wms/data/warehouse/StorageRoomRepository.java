package com.kvbadev.wms.data.warehouse;

import com.kvbadev.wms.models.warehouse.Rack;
import com.kvbadev.wms.models.warehouse.StorageRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRoomRepository {
    List<StorageRoom> findAll();

    List<Rack> getRacks(int storageRoomId);
}
