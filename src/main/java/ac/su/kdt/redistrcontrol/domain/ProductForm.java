package ac.su.kdt.redistrcontrol.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 제출용 DTO - Form
@Getter @Setter
@AllArgsConstructor
public class ProductForm {
    // Form 은 데이터 수신 용도의 DTO 로서 오해가 가장 적은 네이밍 컨벤션으로 폭넓게 쓰임.
    // Form + Seriallizer 쌍을 이루어서 REST API 응답에 많이 사용합니다.
    // 이때, Seriallizer 동작은 JSON String 으로 변환 - Deseriallizer 는 JSON String 을 객체로 다시 파싱하는 동작

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private int salesQuantity;
    private Long categoryId;

    // DTO 생성 시에는 Entity 와의 객체 교환 부분을 함께 구현
    public static ProductForm fromProduct(Product product) {    // 클래스에서 직접 호출 할 수 있게끔 static 선언
        return new ProductForm(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getSalesQuantity(),
                product.getCategory().getId()
        );
    }

    public Product toEntity() { // 객체 메서드? 라 인자 X
        // 바로 new Product 할 때 받아갈 수 있는지 체크
        Category category = new Category();
        category.setId(this.getCategoryId());
        return new Product( // 순서가 꼬이므로 return 마지막에
                this.getId(),
                this.getName(),
                this.getPrice(),
                this.getStockQuantity(),
                this.getSalesQuantity(),
                category
        );
    }
}
