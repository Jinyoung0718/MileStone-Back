package com.sjy.milestone.product.entity;

import com.sjy.milestone.review.entity.Review;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PRODUCTS")
@Getter
@NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Product.detail",
                attributeNodes = {
                        @NamedAttributeNode("category"),
                        @NamedAttributeNode("reviews"),
                        @NamedAttributeNode(value = "productOptions", subgraph = "productOptions.subgraph")
                },
                subgraphs = {
                        @NamedSubgraph(
                                name = "productOptions.subgraph",
                                attributeNodes = {
                                        @NamedAttributeNode("productColor"),
                                        @NamedAttributeNode("productSize")
                                }
                        )
                }
        ),
        @NamedEntityGraph(
                name = "Product.summary",
                attributeNodes = {
                        @NamedAttributeNode("category")
                }
        )
})

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "PRODUCT_NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "PRODUCT_IMAGE_1", nullable = false)
    private String productImg1;

    @Column(name = "PRODUCT_IMAGE_2", nullable = false)
    private String productImg2;

    @Column(name = "PRODUCT_IMAGE_3", nullable = false)
    private String productImg3;

    @Column(name = "PRODUCT_DESCRIPTION", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "PRODUCT_PRICE", nullable = false)
    private Long price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProductOption> productOptions = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;
}