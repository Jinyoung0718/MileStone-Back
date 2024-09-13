package Milestone.spring_project.backend.domain.Entity.Orders;

import Milestone.spring_project.backend.domain.DTO.OrderDTO.OrderItemDTO;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
import Milestone.spring_project.backend.domain.Entity.Item.ProductOption;
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
    public OrderItem(ProductOption productOption, Product product, int quantity, String productName, Long price, String productImg) {
        this.product = product;
        this.productOption = productOption;
        this.quantity = quantity;
        this.productName = product.getName();
        this.price = product.getPrice();
        this.productImg = product.getProductImg1();
    }

    public OrderItemDTO toDTO() {
        return OrderItemDTO.builder()
                .id(this.id)
                .productOption(this.productOption.toDTO())
                .quantity(this.quantity)
                .productName(this.productName)
                .price(this.price)
                .productImg(this.productImg)
                .build();
    }

    public void cancel() {
        this.productOption.increaseStock(this.quantity);
    }
}
