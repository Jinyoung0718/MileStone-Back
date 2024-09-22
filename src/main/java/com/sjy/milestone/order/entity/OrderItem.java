package com.sjy.milestone.order.entity;

import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ORDER_ITEMS")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @Setter
    private Order order;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_OPTION_ID", nullable = false)
    private ProductOption productOption;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String productName;

    @Column(name = "PRICE", nullable = false)
    private Long price;

    @Column(name = "PRODUCT_IMG", nullable = false)
    private String productImg;

    @Builder
    public OrderItem(Product product, ProductOption productOption, int quantity, String productName, Long price, String productImg) {
        this.product = product;
        this.productOption = productOption;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.productImg = productImg;
    }

    public void cancel() {
        this.productOption.increaseStock(this.quantity);
    }
}
