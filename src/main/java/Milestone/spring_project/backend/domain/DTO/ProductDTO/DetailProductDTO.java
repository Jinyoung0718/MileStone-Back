package Milestone.spring_project.backend.domain.DTO.ProductDTO;

import Milestone.spring_project.backend.domain.Entity.Item.Category;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailProductDTO {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String productImg1;
    private String productImg2;
    private String productImg3;
    private String categoryName;
    private List<ReviewDTO> reviews;
    private List<ProductOptionDTO> productOptions;

    public Product toEntity(Category category) {
        return Product.builder()
                .name(this.name)
                .productImg1(this.productImg1)
                .productImg2(this.productImg2)
                .productImg3(this.productImg3)
                .description(this.description)
                .price(this.price)
                .category(category)
                .build();
    }
}
