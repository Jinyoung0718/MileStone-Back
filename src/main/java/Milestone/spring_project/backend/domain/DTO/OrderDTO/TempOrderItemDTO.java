package Milestone.spring_project.backend.domain.DTO.OrderDTO;

import Milestone.spring_project.backend.domain.DTO.ProductDTO.ProductOptionDTO;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
import Milestone.spring_project.backend.domain.Entity.Item.ProductOption;
import Milestone.spring_project.backend.domain.Entity.Orders.OrderItem;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TempOrderItemDTO {

    private Long id;
    private int quantity;
    private ProductOptionDTO productOption;
    private Long price;
    private String productImg;

    public OrderItem toEntity(Product product, ProductOption productOption) {
        return OrderItem.builder()
                .product(product)
                .productOption(productOption)
                .quantity(this.quantity)
                .price(this.price)
                .productImg(this.productImg)
                .build();
    }
}
