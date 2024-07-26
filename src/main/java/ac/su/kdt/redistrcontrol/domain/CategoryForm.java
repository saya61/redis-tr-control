package ac.su.kdt.redistrcontrol.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CategoryForm {
    private Long id;
    private String name;
    int depth;
    private Long parentId;

    public static CategoryForm fromCategory(Category category) {
        return new CategoryForm(
            category.getId(),
            category.getName(),
            category.getDepth(),
            category.getParent().getId()
        );
    }

    public Category toEntity() {
        Category parentCategory = new Category();
        parentCategory.setId(this.getParentId());
        // FK 로 선언된 컬럼에는 대상 테이블 데이터 전체가 필요한게 아니라, PK(id) 값만 넣어줘도 Insert 가능
        //      -> findById() 로 찾아서 넣어주는 것은 비효율적이다. 매번 호출해서 객체 데이터를 채워줄 이유 X

        // findById 메서드 각 카테고리 (DB 기록된) id 값을 핸들링하고 있으면 편리함!
        //      -> Map<String, Long> 형태로 상수형 데이터 스키마에 대해서 응답하는 API 엔드 포인트를 만들어 놓고,
        //      이와 같은 POST 호출해서 구체적인 값(name) 을 id로 치환해서 POST 요청 수행
        return new Category(
            this.getId(),
            this.getName(),
            this.getDepth(),
            (this.getParentId() != null ? parentCategory : null),
                null,
                null
        );
    }
}
