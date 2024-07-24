package ac.su.kdt.redistrcontrol.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private int depth;  // 최상위 1, 그 아래부터 2, 3, 4

    // 1) 가장 단순한 형태로 참조 문제 해결하기(객체 사용 포기)
    private Long parentId;    // 상위 카테고리 객체 타입이 아니라 id 로만 다루기

    // 2) 현재 카테고리에 이르기까지 상위 항목만 리스트로 응답
    // + 리스트 응답시 REST API 설계를 고려해서 응답한 결과로 바로 페이지 호출 가능하도록
    // URL 형태 응답(HyperLink 연속성 추구)
    // 카테고리 엔티티 부터 수술 함.
    public static CategoryResponseDTO fromCategory(Category category) {
        return new CategoryResponseDTO(
            category.getId(),
            category.getName(),
            category.getDepth(),
            category.getParent().getId() != null ? category.getParent().getId() : null
        );
    }
}
