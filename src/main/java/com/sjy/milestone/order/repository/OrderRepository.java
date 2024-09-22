package com.sjy.milestone.order.repository;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.order.entity.Order;
import com.sjy.milestone.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findOrderWithAllByMerchantUid(String merchantUid);

    List<Order> findAllByStatus(OrderStatus orderStatus);

    List<Order> findAllByMemberAndStatusIn(Member member, List<OrderStatus> statuses);
}