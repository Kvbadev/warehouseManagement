package com.kvbadev.wms;

import com.kvbadev.wms.data.ParcelRepository;
import com.kvbadev.wms.models.warehouse.Parcel;
import com.kvbadev.wms.models.warehouse.Product;
import org.instancio.Instancio;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParcelRepositoryTests {
    @Autowired
    private ParcelRepository parcelRepository;

    @Test
    @Order(1)
    @Transactional
    public void saveParcelWithProducts() {
        Product p1 = Instancio.create(Product.class);
        Product p2 = Instancio.create(Product.class);

        Parcel parcel = new Parcel();
        parcel.addProduct(p1);
        parcel.addProduct(p2);

        parcelRepository.save(parcel);

        var savedParcel = parcelRepository.findById(1L).orElseThrow();
        var products = savedParcel.getProducts();
        assert products.size() == 2;
    }
}
