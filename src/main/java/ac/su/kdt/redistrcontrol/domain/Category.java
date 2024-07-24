package ac.su.kdt.redistrcontrol.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int depth;  // 최상위 1, 그 아래부터 2, 3, 4

    @ManyToOne  // ToOne 이 FK 의 정의에 해당한다! 대상 키를 하나만 지정할 수 있으므로!
    @JoinColumn(name = "parent_id")
    private Category parent;    // 상위 카테고리가 존재할 시 parent에 대한 참조를 가짐

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> children;    // 하위 카테고리 목록

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Product> products;    // 하위 상품 목록
}

// 상품 카테고리 예제 / 브랜드패션

//브랜드의류 depth 1
//      - parent : null
//      - children : 브랜드 여성의류, 브랜드 남성의류, 브랜드 캐주얼의류

//브랜드 여성의류 depth 2
//      - parent : 브랜드 의류
//      - children : null
//
//브랜드 남성의류
//      - parent : 브랜드 의류
//      - children : null
//
//브랜드 캐주얼의류
//      - parent : 브랜드 의류
//      - children : null