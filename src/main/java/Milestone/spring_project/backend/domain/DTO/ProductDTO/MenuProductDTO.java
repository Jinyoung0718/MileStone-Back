package Milestone.spring_project.backend.domain.DTO.ProductDTO;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuProductDTO {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String categoryName;
    private String productImg;
}