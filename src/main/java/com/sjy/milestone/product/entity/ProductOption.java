package com.sjy.milestone.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCT_OPTION")
@Getter @NoArgsConstructor
@NamedEntityGraph(
        name = "ProductOption.product",
        attributeNodes = @NamedAttributeNode("product")
)
public class ProductOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_OPTION_ID")
    private Long id;

    @Column(name = "STOCK_QUANTITY")
    private int stockQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_COLOR_ID")
    private ProductColor productColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_SIZE_ID")
    private ProductSize productSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    // option 엔티티를 추가한 이유가 색상과 사이즈의 수량을 조절하기 위하여서 이기에
    // color 와 size 에는 외래키를 소유하지 않고 option 만이 외래키를 소유해도 된다

    // 단순하게 색상별 수량 사이즈별 수량으로 단순 엔티티가 아닌 복합 엔티티로 분할한 이유는
    // 색상과 사이즈 조합별 수량을 정확히 관리하기 위함이다.
    // 빨간색 M 사이즈, 파란색 L 사이즈 수량이 다르듯이 조합별 재고 수량을 얻기 위함이다.

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }
}
