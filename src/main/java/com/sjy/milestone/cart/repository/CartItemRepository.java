package com.sjy.milestone.cart.repository;

import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.cart.entity.CartItem;
import com.sjy.milestone.product.entity.ProductOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"productOption.product", "productOption.productColor", "productOption.productSize"})
    List<CartItem> findByMember(Member member);

    @EntityGraph(attributePaths = {"productOption.product",  "productOption.productColor", "productOption.productSize"})
    List<CartItem> findByMemberAndIdIn(Member member, List<Long> cartItemIds);

    @EntityGraph(attributePaths = {"productOption.product", "productOption.productColor", "productOption.productSize"})
    Optional<CartItem> findByMemberAndProductOption(Member member, ProductOption productOption);
}

