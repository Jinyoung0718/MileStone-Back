package com.sjy.milestone.product;

import com.sjy.milestone.Exception.ProductNotFoundException;
import com.sjy.milestone.product.repository.ProductRepository;
import com.sjy.milestone.product.dto.DetailProductDTO;
import com.sjy.milestone.product.dto.MenuProductDTO;
import com.sjy.milestone.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public List<MenuProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(Product::toMenuProductDTO)
                .toList();
    }

    public DetailProductDTO getProductById(Long productId) {
        Product product = productRepository.findDetailById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
        return product.toDetailProductDTO();
    }

    public List<MenuProductDTO> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName).stream()
                .map(Product::toMenuProductDTO)
                .toList();
    }
}



