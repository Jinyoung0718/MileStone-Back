package com.sjy.milestone.product.mapper;

import com.sjy.milestone.order.dto.temp.TempOrderItemDTO;
import com.sjy.milestone.product.entity.ProductOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductOptionMapper.class})
public interface TempOrderItemMapper {

    @Mapping(source = "productOption", target = "productOption")
    @Mapping(source = "productOption.product.price", target = "price")
    @Mapping(source = "productOption.product.productImg1", target = "productImg")
    @Mapping(source = "quantity", target = "quantity")
    TempOrderItemDTO directToDTO(ProductOption productOption, int quantity);
}


