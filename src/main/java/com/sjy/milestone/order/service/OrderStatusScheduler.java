package com.sjy.milestone.order.service;

import com.sjy.milestone.order.repository.OrderRepository;
import com.sjy.milestone.order.entity.Order;
import com.sjy.milestone.order.entity.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j @Transactional
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public void updateOrderStatus(Order order, OrderStatus nextStatus, String statusMessage) {

        if (order.getStatus() == nextStatus) {
            return;
        }

        order.setStatus(nextStatus);
        orderRepository.save(order);

        redisTemplate.convertAndSend("order/status", order.getMember().getUserEmail() + ": 주문 상태 - " + statusMessage);
    }

    public void triggerOrderUpdates() {
        processOrders(OrderStatus.ORDERED, OrderStatus.PROCS, "주문 처리 중");
        processOrders(OrderStatus.PROCS, OrderStatus.SHIPPED, "배송 중");
        processOrders(OrderStatus.SHIPPED, OrderStatus.DELIVERED, "배송 완료");
    }

    private void processOrders(OrderStatus currentStatus, OrderStatus nextStatus, String statusMessage) {
        List<Order> ordersToProcess = orderRepository.findAllByStatus(currentStatus);

        if (!ordersToProcess.isEmpty()) {
            ordersToProcess.forEach(order -> updateOrderStatus(order, nextStatus, statusMessage));
        }
    }
}