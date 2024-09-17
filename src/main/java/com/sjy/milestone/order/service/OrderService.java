package com.sjy.milestone.order.service;

import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.account.validator.MemberValidator;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.cart.entity.CartItem;
import com.sjy.milestone.cart.repository.CartItemRepository;
import com.sjy.milestone.exception.badrequest.InsufficientStockException;
import com.sjy.milestone.exception.notfound.OrderNotFoundException;
import com.sjy.milestone.exception.notfound.ProductOptionNotFoundException;
import com.sjy.milestone.exception.unauthorized.UnauthorizedException;
import com.sjy.milestone.product.entity.ProductOption;
import com.sjy.milestone.product.repository.ProductOptionRepository;
import com.sjy.milestone.order.dto.DirectOrderDTO;
import com.sjy.milestone.order.dto.OrderDTO;
import com.sjy.milestone.order.dto.TempOrderDTO;
import com.sjy.milestone.order.dto.TempOrderItemDTO;
import com.sjy.milestone.order.entity.Order;
import com.sjy.milestone.order.entity.OrderStatus;
import com.sjy.milestone.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class OrderService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final PaymentService paymentService;
    private final OrderManagementService orderManagementService;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderStatusScheduler orderStatusScheduler;
    private final OrderRepository orderRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public TempOrderDTO getCartItemsByIds(String userEmail, List<Long> cartItemIds) {
        Member member = memberRepository.findMemberWithOrdersByUserEmail(userEmail);
        memberValidator.validateMember(member);
        List<CartItem> cartItems = cartItemRepository.findByMemberAndIdIn(member, cartItemIds);

        List<TempOrderItemDTO> tempOrderItems = cartItems.stream()
                .map(CartItem::toTempOrderItemDTO)
                .toList();

        Long totalPrice = tempOrderItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();

        return TempOrderDTO.of(tempOrderItems, totalPrice);
    }

    public TempOrderDTO getDirectOrderItem(String userEmail, DirectOrderDTO directOrderDTO) {
        Member member = memberRepository.findMemberWithOrdersByUserEmail(userEmail);
        memberValidator.validateMember(member);
        ProductOption productOption = productOptionRepository.findProductOptionWithProductById(directOrderDTO.getProductOptionId())
                .orElseThrow(() -> new ProductOptionNotFoundException("상품 옵션 정보를 찾을 수 없습니다"));

        if (productOption.getStockQuantity() < directOrderDTO.getQuantity()) {
            throw new InsufficientStockException("재고가 부족합니다");
        }

        TempOrderItemDTO tempOrderItem = productOption.toTempOrderItemDTO(directOrderDTO.getQuantity());
        Long totalPrice = productOption.getProduct().getPrice() * directOrderDTO.getQuantity();
        return TempOrderDTO.of(List.of(tempOrderItem), totalPrice);
    }

    public String placeOrder(String userEmail, TempOrderDTO tempOrderDTO) {
        Member member = memberRepository.findMemberWithOrdersByUserEmail(userEmail);
        memberValidator.validateMember(member);
        String merchantUid = generateMerchantUid();

        Order order = orderManagementService.createOrder(member, merchantUid, tempOrderDTO);
        paymentService.preparePayment(merchantUid, tempOrderDTO.getTotalPrice());

        tempOrderDTO.setId(order.getId());
        return merchantUid;
    }

    public void completeOrder(String impUid, String merchantUid) {
        try {
            paymentService.verifyPayment(impUid, merchantUid);
            Order order = orderManagementService.findOrderWithAllByMerchantUid(merchantUid.trim());
            order.setImpUid(impUid);
            order.setStatus(OrderStatus.ORDERED);
            orderRepository.save(order);
            orderStatusScheduler.triggerOrderUpdates();
        } catch (Exception e) {
            log.error("주문 완료 중 예외 발생: {}", e.getMessage(), e);
        }
    }

    public void cancelOrder(String userEmail, Long orderId) {
        Member member = memberRepository.findByUserEmail(userEmail);
        memberValidator.validateMember(member);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다"));

        if (!order.getMember().equals(member)) {
            throw new UnauthorizedException("해당 주문을 취소할 권한이 없습니다");
        }

        paymentService.requestRefund(order.getImpUid());
        orderManagementService.cancelOrder(order);
        redisTemplate.convertAndSend("order-status", order.getMember().getUserEmail()  + ": 주문 상태 - " + OrderStatus.CANCELLED);
    }

    private String generateMerchantUid() {
        String uniqueString = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format = today.format(formatter);

        return format + '-' + uniqueString;
    }

    public List<OrderDTO> getOrdersByUserEmail(String userEmail) {
        Member member = memberRepository.findByUserEmail(userEmail);
        memberValidator.validateMember(member);
        List<Order> orders = orderRepository.findAllByMemberAndStatusIn(member, List.of(
                OrderStatus.ORDERED,
                OrderStatus.PROCS,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED
        ));

        return orders.stream()
                .map(Order::toDTO)
                .toList();
    }
}