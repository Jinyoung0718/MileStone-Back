package com.sjy.milestone.product.mapper;

import com.sjy.milestone.product.dto.DetailProductDTO;
import com.sjy.milestone.product.dto.MenuProductDTO;
import com.sjy.milestone.product.entity.Product;
import com.sjy.milestone.review.mapper.ReviewMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductOptionMapper.class, ReviewMapper.class})
public interface ProductMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "productImg1", source = "productImg1")
    @Mapping(target = "productImg2", source = "productImg2")
    @Mapping(target = "productImg3", source = "productImg3")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "reviews", source = "reviews")
    @Mapping(target = "productOptions", source = "productOptions")
    DetailProductDTO toDetailProductDTO(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "productImg", source = "productImg1")
    MenuProductDTO toMenuProductDTO(Product product);
}
