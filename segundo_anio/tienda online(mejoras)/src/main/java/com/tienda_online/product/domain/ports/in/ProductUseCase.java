package com.tienda_online.product.domain.ports.in;

import java.util.List;

import com.tienda_online.product.application.dto.ProductDto;

public interface ProductUseCase {

    List<ProductDto> getAllProducts(Long categoryId);

    ProductDto getProductById(Long id);

    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);
}
