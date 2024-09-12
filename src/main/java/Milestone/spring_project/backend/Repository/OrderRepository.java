package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Orders.Order;
import Milestone.spring_project.backend.domain.Entity.Orders.OrderStatus;
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