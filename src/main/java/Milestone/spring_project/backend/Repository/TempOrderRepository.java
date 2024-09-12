package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Orders.TempOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempOrderRepository extends JpaRepository<TempOrder, Long> {
}
