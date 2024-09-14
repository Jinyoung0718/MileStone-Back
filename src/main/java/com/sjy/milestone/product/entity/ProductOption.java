package com.sjy.milestone.product.entity;

import com.sjy.milestone.order.dto.TempOrderItemDTO;
import com.sjy.milestone.product.dto.ProductOptionDTO;
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
    // color와 size에는 외래키를 소유하지 않고 option만이 외래키를 소유해도 된다

    // 단순하게 색상별 수량 사이즈별 수량으로 단순 엔티티가 아닌 복합 엔티티로 분할한 이유는
    // 색상과 사이즈 조합별 수량을 정확히 관리하기 위함이다.
    // 빨간색 M 사이즈, 파란색 L사이즈 수량이 다르듯이 조합별 재고 수량을 얻기 위함이다.

    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 재고가 0이 될 경우 프론트에서 선택하지 못하게 처리할 예정, 하지만
    // 백엔드에서 최종적으로 재고 상태를 확인하여 데이터 무결성을 보장해야 됨
    // 백엔드에서 추가적인 검증을 통해 시스템의 안전성을 강화해야 됨

    public ProductOptionDTO toDTO() {
        return ProductOptionDTO.builder()
                .id(this.id)
                .stockQuantity(this.stockQuantity)
                .color(this.productColor.getColor())
                .size(this.productSize.getSizeName())
                .build();
    }

    public TempOrderItemDTO toTempOrderItemDTO(int quantity) {
        return TempOrderItemDTO.builder()
                .productOption(this.toDTO())
                .quantity(quantity)
                .price(this.product.getPrice())
                .productImg(this.product.getProductImg1())
                .build();
    }
}
