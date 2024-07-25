package ac.su.kdt.redistrcontrol.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@AllArgsConstructor
// Entity 로 Spring Persistence 라이브러리에서 요구하는  사항 - No~
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;

    // 재고량 및 판매량
    private int stockQuantity;
    private int salesQuantity;

    // 카테고리
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
