package com.sjy.milestone.product.dto;

import com.sjy.milestone.review.dto.ReviewDTO;
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
}
