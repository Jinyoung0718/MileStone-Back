package Milestone.spring_project.backend.domain.DTO.ProductDTO;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductOptionDTO {
    private Long id;
    private Integer stockQuantity;
    private String color;
    private String size;
}
