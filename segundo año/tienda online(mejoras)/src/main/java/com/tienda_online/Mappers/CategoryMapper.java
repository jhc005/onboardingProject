package com.tienda_online.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.tienda_online.DTO.CategoryDto;
import com.tienda_online.domain.entities.Category;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    void update(CategoryDto categoryDto, @MappingTarget Category category);
}
