package com.tienda_online.category.domain.ports.out;

import java.util.List;
import com.tienda_online.category.domain.model.Category;

public interface CategoryRepositoryPort {

    List<Category> findAll();

    Category findById(Long id); 

    Category save(Category category);

}
