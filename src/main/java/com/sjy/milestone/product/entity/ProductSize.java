package com.sjy.milestone.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "PRODUCT_SIZE")
@Getter @NoArgsConstructor
public class ProductSize {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_SIZE_ID")
    private Long id;

    @Column(name = "SIZE_NAME", length = 10, nullable = false)
    private String sizeName;
}
