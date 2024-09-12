package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Item.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

     @EntityGraph(value = "Product.summary", type = EntityGraph.EntityGraphType.LOAD) @NotNull
     List<Product> findAll();

     @EntityGraph(value = "Product.summary", type = EntityGraph.EntityGraphType.LOAD)
     List<Product> findByCategoryName(String categoryName);

     @EntityGraph(value = "Product.detail", type = EntityGraph.EntityGraphType.LOAD)
     Optional<Product> findDetailById(@Param("productId") Long productId);

}

// "/api/products/category/Outerwear" 와 같이 가독성과 이름으로 접근하는 것이 직관적이며
// 카테고리 ID는 변경될 가능성이 있지만 이름은 변경될 가능성이 적다 -> 추후 1차 카테고리에 이어 2차 카테고리 염두