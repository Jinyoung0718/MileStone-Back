package com.sjy.milestone.order.service;

import com.sjy.milestone.Exception.OrderNotFoundException;
import com.sjy.milestone.Exception.ProductOptionNotFoundException;
import com.sjy.milestone.cart.repository.CartItemRepository;
import com.sjy.milestone.auth.repository.MemberRepository;
import com.sjy.milestone.order.repository.OrderRepository;
import com.sjy.milestone.product.repository.ProductOptionRepository;
import com.sjy.milestone.auth.validator.MemberValidator;
import com.sjy.milestone.order.dto.TempOrderDTO;
import com.sjy.milestone.order.dto.TempOrderItemDTO;
import com.sjy.milestone.auth.entity.Member;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.order.entity.Order;
import com.sjy.milestone.order.entity.OrderItem;
import com.sjy.milestone.order.entity.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class OrderManagementService {

    private final OrderRepository orderRepository;
    private final ProductOptionRepository productOptionRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberValidator memberValidator;

    public Order createOrder(Member member, String merchantUid, TempOrderDTO tempOrderDTO) {
        Order order = Order.fromDTO(member, merchantUid, tempOrderDTO);

        for (TempOrderItemDTO tempOrderItemDTO : tempOrderDTO.getTempOrderItems()) {
            ProductOption productOption = productOptionRepository.findProductOptionWithProductById(tempOrderItemDTO.getProductOption().getId())
                    .orElseThrow(() -> new ProductOptionNotFoundException("해당 상품 옵션 정보를 찾을 수 없습니다"));
            productOption.decreaseStock(tempOrderItemDTO.getQuantity()); // 동시성 처리

            OrderItem orderItem = tempOrderItemDTO.toEntity(productOption.getProduct(), productOption);
            order.addOrderItem(orderItem);

            if (tempOrderItemDTO.getId() != null) {
                cartItemRepository.findById(tempOrderItemDTO.getId()).ifPresent(cartItemRepository::delete);
            }
        }
        orderRepository.save(order);
        return order;
    }

    public void deleteOrder(String userEmail, Long orderId) {
        Member member = memberRepository.findMemberWithOrdersByUserEmail(userEmail);
        memberValidator.validateMember(member);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다"));

        if (!order.getStatus().equals(OrderStatus.DELIVERED)) {
            throw new IllegalStateException("주문상태가 맞지않습니다");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
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