package com.sjy.milestone.cart.mapper;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.cart.dto.CartItemDTO;
import com.sjy.milestone.cart.dto.CartItemRequestDTO;
import com.sjy.milestone.cart.entity.CartItem;
import com.sjy.milestone.order.dto.temp.TempOrderItemDTO;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.product.mapper.ProductOptionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductOptionMapper.class})
public interface CartItemMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "productName", source = "productOption.product.name")
    @Mapping(target = "productOption", source = "productOption")
    @Mapping(target = "productImg", source = "productOption.product.productImg1")
    CartItemDTO toCartItemDTO(CartItem cartItem);

    @Mapping(target = "member", source = "member")
    @Mapping(target = "productOption", source = "productOption")
    @Mapping(target = "quantity", source = "cartItemRequestDTO.quantity")
    CartItem toCartItem(CartItemRequestDTO cartItemRequestDTO, Member member, ProductOption productOption);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "productOption", source = "productOption")
    @Mapping(target = "price", source = "productOption.product.price")
    @Mapping(target = "productImg", source = "productOption.product.productImg1")
    TempOrderItemDTO toTempOrderItemFromCart(CartItem cartItem);
}

