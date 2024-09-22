package com.sjy.milestone.order.service;

import com.sjy.milestone.exception.notfound.OrderNotFoundException;
import com.sjy.milestone.exception.notfound.ProductOptionNotFoundException;
import com.sjy.milestone.cart.repository.CartItemRepository;
import com.sjy.milestone.order.mapper.OrderItemMapper;
import com.sjy.milestone.order.mapper.OrderMapper;
import com.sjy.milestone.order.repository.OrderRepository;
import com.sjy.milestone.product.repository.ProductOptionRepository;
import com.sjy.milestone.order.dto.temp.TempOrderDTO;
import com.sjy.milestone.order.dto.temp.TempOrderItemDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.order.entity.Order;
import com.sjy.milestone.order.entity.OrderItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class OrderManagementService {

    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final CartItemRepository cartItemRepository;

    public Order createOrder(Member member, String merchantUid, TempOrderDTO tempOrderDTO) {
        Order order = orderMapper.toOrder(tempOrderDTO, member, merchantUid);

        for (TempOrderItemDTO tempOrderItemDTO : tempOrderDTO.getTempOrderItems()) {
            ProductOption productOption = productOptionRepository.findProductOptionWithProductById(tempOrderItemDTO.getProductOption().getId())
                    .orElseThrow(() -> new ProductOptionNotFoundException("해당 상품 옵션 정보를 찾을 수 없습니다"));

            productOption.decreaseStock(tempOrderItemDTO.getQuantity()); // 비관적 락

            OrderItem orderItem = orderItemMapper.toOrderItem(tempOrderItemDTO, productOption);
            order.addOrderItem(orderItem);

            if (tempOrderItemDTO.getId() != null) {
                cartItemRepository.findById(tempOrderItemDTO.getId()).ifPresent(cartItemRepository::delete);
            }
        }
        orderRepository.save(order);
        return order;
    }

    public void cancelOrder(Order order) {
        order.cancel();
        orderRepository.save(order);
    }

    public Order findOrderWithAllByMerchantUid(String merchantUid) {
        return orderRepository.findOrderWithAllByMerchantUid(merchantUid.trim())
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));
    }
}