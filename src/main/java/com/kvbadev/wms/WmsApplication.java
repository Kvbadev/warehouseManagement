package com.kvbadev.wms;

import com.kvbadev.wms.data.ItemRepository;
import com.kvbadev.wms.data.ParcelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WmsApplication {
    private static final Logger log = LoggerFactory.getLogger(WmsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }

    public static CommandLineRunner seedDatabase(ParcelRepository parcelRepository, ItemRepository itemRepository) {
        return (args) -> {
//            itemRepository.save(new Item("Item-testowy1"));
            //HOW TO ADD AN ENTITY, WHICH ONE GOTTA BE FIRST
//            packageRepository.save(new Parcel("Paczka-testowa1", 2400, itemRepository.findAll()));
        };
    }
}
