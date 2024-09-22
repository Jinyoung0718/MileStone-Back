package com.sjy.milestone.product.mapper;

import com.sjy.milestone.product.dto.ProductOptionDTO;
import com.sjy.milestone.product.entity.ProductOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductOptionMapper {

    @Mapping(target = "color", source = "productColor.color")
    @Mapping(target = "size", source = "productSize.sizeName")
    ProductOptionDTO toDTO(ProductOption productOption);
}
