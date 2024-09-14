package com.sjy.milestone.product.controller;

import com.sjy.milestone.product.ProductService;
import com.sjy.milestone.product.dto.DetailProductDTO;
import com.sjy.milestone.product.dto.MenuProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<MenuProductDTO>> getAllProducts() {
        List<MenuProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<DetailProductDTO> getProductById(@PathVariable Long productId) {
        DetailProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<MenuProductDTO>> getProductByCategory(@PathVariable String categoryName) {
        List<MenuProductDTO> products = productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(products);
    }
}
