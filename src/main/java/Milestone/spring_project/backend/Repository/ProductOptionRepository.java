package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Item.ProductOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    @EntityGraph(attributePaths = {"product"})
    Optional<ProductOption> findProductOptionWithProductById(Long id);
}
