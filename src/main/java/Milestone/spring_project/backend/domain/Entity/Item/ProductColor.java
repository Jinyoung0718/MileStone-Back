package Milestone.spring_project.backend.domain.Entity.Item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "PRODUCT_COLORS")
@Getter @NoArgsConstructor
public class ProductColor {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_COLOR_ID")
    private Long id;

    @Column(name = "COLOR", length = 50, nullable = false)
    private String color;
}
