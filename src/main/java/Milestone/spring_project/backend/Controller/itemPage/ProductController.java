package Milestone.spring_project.backend.Controller.itemPage;

import Milestone.spring_project.backend.Service.itemService.ProductService;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.DetailProductDTO;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.MenuProductDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<DetailProductDTO> getProductById(@PathVariable Long id) {
        DetailProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<MenuProductDTO>> getProductByCategory(@PathVariable String categoryName) {
        List<MenuProductDTO> products = productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(products);
    }
}
