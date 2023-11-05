package com.kvbadev.wms.data;

import com.kvbadev.wms.models.Rack;
import com.kvbadev.wms.models.StorageRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StorageRoomRepository {
    List<StorageRoom> findAll();

    List<Rack> getRacks(int storageRoomId);
}
