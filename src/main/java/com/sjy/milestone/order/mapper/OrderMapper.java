package com.sjy.milestone.order.mapper;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.order.dto.order.OrderDTO;
import com.sjy.milestone.order.dto.temp.TempOrderDTO;
import com.sjy.milestone.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "merchantUid", source = "merchantUid")
    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "totalPrice", source = "totalPrice")
    @Mapping(target = "userEmail", source = "member.userEmail")
    @Mapping(target = "requestMessage", source = "requestMessage")
    @Mapping(target = "recipientName", source = "recipientName")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "deliveryDetail", source = "deliveryDetail")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "orderDate", source = "orderDate")
    OrderDTO toOrderDTO(Order order);

    @Mapping(source = "dto.totalPrice", target = "totalPrice")
    @Mapping(source = "dto.requestMessage", target = "requestMessage")
    @Mapping(source = "dto.recipientName", target = "recipientName")
    @Mapping(source = "dto.deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "dto.deliveryDetail", target = "deliveryDetail")
    @Mapping(source = "dto.deliveryZipcode", target = "deliveryZipcode")
    @Mapping(source = "dto.phoneNumber", target = "phoneNumber")
    @Mapping(source = "merchantUid", target = "merchantUid")
    @Mapping(target = "status", constant  = "PENDING")
    Order toOrder(TempOrderDTO dto, Member member, String merchantUid);
}