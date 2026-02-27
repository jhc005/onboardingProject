package com.tienda_online.product.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tienda_online.product.application.dto.ProductDto;
import com.tienda_online.product.application.inport.ProductUseCase;
import com.tienda_online.product.domain.model.Product;
import com.tienda_online.product.domain.outport.ProductRepositoryPort;
import com.tienda_online.category.domain.model.Category;
import com.tienda_online.category.domain.model.outport.CategoryRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepository;
    private final CategoryRepositoryPort categoryRepository;

    @Override
    public List<ProductDto> getAllProducts(Long categoryId) {
        List<Product> products = (categoryId != null)
            ? productRepository.findByCategoryId(categoryId)
            : productRepository.findAll();

        return products.stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getCategory().getId(),
                        p.getStock(),
                        p.getUpdateDate()))
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product p = productRepository.findById(id);
        return new ProductDto(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getCategory().getId(),
                p.getStock(),
                p.getUpdateDate());
    }

    @Override
    public ProductDto createProduct(ProductDto dto) {

        Category category = categoryRepository.findById(dto.getCategoryId());

        Product product = new Product(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                category,
                dto.getStock());

        Product saved = productRepository.save(product);

        return new ProductDto(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getPrice(),
                saved.getCategory().getId(),
                saved.getStock(),
                saved.getUpdateDate());
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product existing = productRepository.findById(id);

        Category category = categoryRepository.findById(dto.getCategoryId());

        existing.update(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                category,
                dto.getStock()
        );

        Product updated = productRepository.save(existing);

        return new ProductDto(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getPrice(),
                updated.getCategory().getId(),
                updated.getStock(),
                updated.getUpdateDate());
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id);
        productRepository.delete(product);
    }
}