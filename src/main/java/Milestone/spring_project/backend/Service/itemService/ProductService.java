package Milestone.spring_project.backend.Service.itemService;

import Milestone.spring_project.backend.Exception.ProductNotFoundException;
import Milestone.spring_project.backend.Repository.ProductRepository;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.DetailProductDTO;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.MenuProductDTO;
import Milestone.spring_project.backend.domain.Entity.Item.Product;
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

    public DetailProductDTO getProductById(Long id) {
        Product product = productRepository.findDetailById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다"));
        return product.toDetailProductDTO();
    }

    public List<MenuProductDTO> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName).stream()
                .map(Product::toMenuProductDTO)
                .toList();
    }
}



