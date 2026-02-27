package com.tienda_online.product.domain.outport;

import java.util.List;
import com.tienda_online.product.domain.model.Product;

public interface ProductRepositoryPort {

    List<Product> findAll();

    List<Product> findByCategoryId(Long categoryId);

    Product findById(Long id);

    Product save(Product product);

    void delete(Product product);
}
