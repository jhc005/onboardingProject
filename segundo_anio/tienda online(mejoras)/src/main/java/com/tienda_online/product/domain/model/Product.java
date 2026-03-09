package com.tienda_online.product.domain.model;

import java.time.LocalDateTime;
import com.tienda_online.category.domain.model.Category;

public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private Category category;
    private Integer stock;
    private LocalDateTime updateDate;

    public Product(Long id, String name, String description,
                   Double price, Category category, Integer stock,
                   LocalDateTime updateDate) {
        validateName(name);
        validatePriceAndStock(price, stock);
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.updateDate = updateDate;
    }

    public Product(String name, String description,
                   Double price, Category category, Integer stock) {
        this(null, name, description, price, category, stock, LocalDateTime.now());
    }

    private void validateName(String name) {
        if (name == null || name.length() < 3 || name.length() > 100) {
            throw new IllegalArgumentException("Product name must be between 3 and 100 characters");
        }
    }

    private void validatePriceAndStock(Double price, Integer stock) {
        if (stock != null && stock > 0 && (price == null || price <= 0)) {
            throw new IllegalArgumentException(
                "Product price must be greater than zero if stock is greater than zero"
            );
        }
    }

    public void update(String name, String description, Double price,
                       Category category, Integer stock) {
        validateName(name);
        validatePriceAndStock(price, stock);
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.stock = stock;
        this.updateDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Category getCategory() { return category; }
    public Integer getStock() { return stock; }
    public LocalDateTime getUpdateDate() { return updateDate; }
}
