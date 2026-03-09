package com.tienda_online.category.domain.model;

public class Category {

    private Long id;
    private String name;

    public Category(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (name == null || name.length() < 3 || name.length() > 100) {
            throw new IllegalArgumentException(
                "Category name must be between 3 and 100 characters"
            );
        }
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
