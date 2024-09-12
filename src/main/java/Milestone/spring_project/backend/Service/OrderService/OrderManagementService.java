package Milestone.spring_project.backend.Service.OrderService;

import Milestone.spring_project.backend.Exception.OrderNotFoundException;
import Milestone.spring_project.backend.Exception.ProductOptionNotFoundException;
import Milestone.spring_project.backend.Repository.CartItemRepository;
import Milestone.spring_project.backend.Repository.MemberRepository;
import Milestone.spring_project.backend.Repository.OrderRepository;
import Milestone.spring_project.backend.Repository.ProductOptionRepository;
import Milestone.spring_project.backend.Util.Validator.Member.MemberValidator;
import Milestone.spring_project.backend.domain.DTO.OrderDTO.TempOrderDTO;
import Milestone.spring_project.backend.domain.DTO.OrderDTO.TempOrderItemDTO;
import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Item.ProductOption;
import Milestone.spring_project.backend.domain.Entity.Orders.Order;
import Milestone.spring_project.backend.domain.Entity.Orders.OrderItem;
import Milestone.spring_project.backend.domain.Entity.Orders.OrderStatus;
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
            productOption.decreaseStock(tempOrderItemDTO.getQuantity());

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