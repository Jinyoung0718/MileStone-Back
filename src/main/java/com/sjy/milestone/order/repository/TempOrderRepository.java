package com.sjy.milestone.order.repository;

import com.sjy.milestone.order.entity.TempOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempOrderRepository extends JpaRepository<TempOrder, Long> {
}
