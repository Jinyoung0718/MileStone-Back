package com.sjy.milestone.order.mapper;

import com.sjy.milestone.order.dto.order.OrderItemDTO;
import com.sjy.milestone.order.dto.temp.TempOrderItemDTO;
import com.sjy.milestone.order.entity.OrderItem;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.product.mapper.ProductOptionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductOptionMapper.class})
public interface OrderItemMapper {


    @Mapping(target = "product", source = "productOption.product")
    @Mapping(target = "productOption", source = "productOption")
    @Mapping(target = "quantity", source = "tempOrderItemDTO.quantity")
    @Mapping(target = "price", source = "tempOrderItemDTO.price")
    @Mapping(target = "productImg", source = "tempOrderItemDTO.productImg")
    @Mapping(target = "productName", source = "productOption.product.name")
    OrderItem toOrderItem(TempOrderItemDTO tempOrderItemDTO, ProductOption productOption);


    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productOption", source = "productOption")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "productImg", source = "productImg")
    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}