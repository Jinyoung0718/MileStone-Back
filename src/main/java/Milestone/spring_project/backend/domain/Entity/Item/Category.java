package Milestone.spring_project.backend.domain.Entity.Item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "CATEGORYS")
@Getter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}

// 카테고리 아이디를 통해 카테고리 이름으로 분류하는 방식
// 데이터베이스에 상품을 삽입할 때, 각 상품은 CATEGORY_ID를 통해 특정 카테고리와 연관

// 카테로기 이름으로 분류할 때 과정 정리
// 1. 먼저 카테고리 이름을 통해 해당 카테고리를 조회
// 2. 조회한 카테고리의 ID를 사용하여 해당 카테고리에 속한 상품들을 조회