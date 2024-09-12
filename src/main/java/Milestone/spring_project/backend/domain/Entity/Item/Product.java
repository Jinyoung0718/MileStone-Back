package Milestone.spring_project.backend.domain.Entity.Item;

import Milestone.spring_project.backend.domain.DTO.ProductDTO.DetailProductDTO;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.MenuProductDTO;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.ProductOptionDTO;
import Milestone.spring_project.backend.domain.DTO.ProductDTO.ReviewDTO;
import Milestone.spring_project.backend.domain.Entity.CommentAndReview.Review;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.List;
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

    @Column(name = "PRODUCT_NAME", nullable = false)
    private String name;

    @Column(name = "PRODUCT_IMAGE_1", nullable = false)
    private String productImg1;

    @Column(name = "PRODUCT_IMAGE_2", nullable = false)
    private String productImg2;

    @Column(name = "PRODUCT_IMAGE_3", nullable = false)
    private String productImg3;

    @Column(name = "PRODUCT_DESCRIPTION", nullable = false)
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


    @Builder
    public Product(String name, String productImg1, String productImg2, String productImg3, String description, Long price, Category category) {
        this.name = name;
        this.productImg1 = productImg1;
        this.productImg2 = productImg2;
        this.productImg3 = productImg3;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public MenuProductDTO toMenuProductDTO() {
        return MenuProductDTO.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .categoryName(this.category.getName())
                .productImg(this.productImg1)
                .build();
    }

    public DetailProductDTO toDetailProductDTO() {

        List<ReviewDTO> reviewDTOS = this.reviews.stream()
                .map(Review::toDTO)
                .toList();

        // 상세 상품 페이지 요청 시, 상품 내용과 함께 리뷰 내용을 보여주기 위함

        List<ProductOptionDTO> productOptionDTOS = this.productOptions.stream()
                .map(ProductOption::toDTO)
                .toList();

        // 상세 상품 페이지 요청 시, 색상, 사이즈 조합별 수량을 나타내기 위함

        return DetailProductDTO.builder()
                .id(this.id)
                .name(this.name)
                .productImg1(this.productImg1)
                .productImg2(this.productImg2)
                .productImg3(this.productImg3)
                .description(this.description)
                .price(this.price)
                .reviews(reviewDTOS)
                .productOptions(productOptionDTOS)
                .categoryName(this.category.getName())
                .build();
    }
}

// product Id를 안 주었는데 어떻게 상품의 댓글을 구분하냐는 궁금증이 생김 ->
// 엔티티 그 자체 내부에서 정의되어 있는 필드이면서 제약조건이 있기에, 해당 필드를 map 함수를 이용해 DTO 화 시켜도 상품을 구분함 (해당 필드 자체가 상품을 구분하기 때문)

// 2024 - 08 - 06 : 엔티티 그래프를 사용하여서 sql 쿼리의 빈도를 줄일 수 있었다.
// @NamedEntityGraph 는 특정 연관된 엔티티들을 함께 로딩하기 위한 엔티티 그래프를 정의한다.
// 해당 엔티티를 조회할 때 연관된 엔티티들을 한 번에 로딩할 수 있도록 설정되며, N+1문제를 피할 수 있다.
// 이후 레포지토리에서 메서드를 설정할 때, @EntityGraph(attributePaths = {"product"}) 로 하게 되면