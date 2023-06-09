package com.kvbadev.wms;

import com.kvbadev.wms.data.ParcelRepository;
import com.kvbadev.wms.models.warehouse.Product;
import com.kvbadev.wms.models.warehouse.Parcel;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParcelRepositoryTests {
    @Autowired
    private ParcelRepository parcelRepository;

    @Test
    @Order(1)
    @Transactional
    public void saveParcelWithProducts() {
        Product product1 = new Product("p1", "description1", BigDecimal.valueOf(11.40));
        Product product2 = new Product("p2", "description2", BigDecimal.valueOf(21.30));

        Parcel parcel = new Parcel();
        parcel.addProduct(product1);
        parcel.addProduct(product2);

        parcelRepository.save(parcel);

        var savedParcel = parcelRepository.findById(1L).orElseThrow();
        var products = savedParcel.getProducts();
        assert products.size() == 2;
        assert Objects.equals(products.get(0).getName(), "p1");
        assert Objects.equals(products.get(1).getPrice(), BigDecimal.valueOf(21.30));
    }
}
