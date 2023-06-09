package com.kvbadev.wms;

import com.kvbadev.wms.data.ProductRepository;
import com.kvbadev.wms.models.warehouse.Product;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    @Test
    @Order(1)
    public void saveOrUpdateProduct() {
        Product product = new Product("Test product", "Test description", BigDecimal.valueOf(44.50));
        productRepository.save(product);
        var res = productRepository.findAll();
        product = res.get(0);

        assert product != null;
        assert product.getId() == 1;
        assert product.getPrice().doubleValue() == 44.50;
    }

    @Test
    @Order(2)
    public void removeProduct() {
        var product = productRepository.findById(1L).orElseThrow();

        productRepository.delete(product);

        assert productRepository.findById(1L).isEmpty();
    }
}
