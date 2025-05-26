package com.tienda_online.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.tienda_online.DTO.ProductDto;
import com.tienda_online.domain.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void update(ProductDto productDto, @MappingTarget Product product);
}
