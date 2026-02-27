package com.tienda_online.category.domain.model.outport;

import java.util.List;
import com.tienda_online.category.domain.model.Category;

public interface CategoryRepositoryPort {

    List<Category> findAll();

    Category findById(Long id); 

    Category save(Category category);

}
