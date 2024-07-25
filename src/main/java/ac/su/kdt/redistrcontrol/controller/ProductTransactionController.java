package ac.su.kdt.redistrcontrol.controller;

import ac.su.kdt.redistrcontrol.domain.Product;
import ac.su.kdt.redistrcontrol.domain.ProductForm;
import ac.su.kdt.redistrcontrol.service.ProductService;
import ac.su.kdt.redistrcontrol.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products-transaction")
public class ProductTransactionController {
    // redis에서 발급받은 키 제출되어야만 후속 create 로직 수행하도록 엔트포인트 구현
    private final RedisService redisService;
    private final ProductService productService;

    // 과제 2) ProductTransactionController 를 만들고,
    //     redis 에서 발급받은 키가 제출되어야만 후속 create 로직 수행하도록 엔드포인트 구현
    // POST 요청 전에 Transaction Key를 발급된 것을 전제로 함
    @PostMapping
    public ResponseEntity<Product> createProduct(
            // url 은 kebab-case, 변수명은 camelCase
            @RequestParam(name = "transaction-key") String transactionKey,  // 트랜잭션 키 받아오기
            @RequestBody ProductForm product
    ) {
        // 요청 수신 후 Transaction Key 부터 검사
        boolean isTransactionSuccess = redisService.setIfAbsent(
                transactionKey, LocalDateTime.now().toString()
        );
        if (!isTransactionSuccess) {
            try {
                Product createdProduct = productService.createProduct(product.toEntity());
                return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // 이미 요청된 상품 생성 Transaction Key 가 재수신된 경우 에러 응답!
        // => 에러응답으로는 충분하지 않다!
        //      ( 고객의 에러 페이지 수신 시 뒤로가기 및 재호출을 수많은 횟수 반복하면서 결국 TTL 초과하여 호출 됨.)
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/with-cache")
    public ResponseEntity<Product> createProduct2(  // 복잡도가 높아 다른 코드 흐름으로 대체
            // url 은 kebab-case, 변수명은 camelCase
            @RequestParam(name = "transaction-key") String transactionKey,  // 트랜잭션 키 받아오기
            @RequestBody ProductForm product
    ) {
        // 요청 수신 후 Transaction Key 부터 검사
        Optional<Product> createteProduct = redisService.setIfAbsentGetIfPresent(
                transactionKey,
//                LocalDateTime.now().toString()
                product.toEntity()
        );
        if (!createteProduct.isEmpty()) {
            try {
                Product createdProduct = productService.createProduct(product.toEntity());
                return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(createteProduct.get(), HttpStatus.CREATED);
    }

    @PostMapping("/with-cache2")
    public ResponseEntity<Product> createProduct3(
            @RequestParam(name = "transaction-key") String transactionKey,
            @RequestBody ProductForm product
    ) {
        //
        // 1) transatcionKey 에 대한 캐시값이 있는지 확인(get 호출)
        //    1-1) 없는 경우 정상 호출 진행
        //       1-2-1) 정상 흐름 진행 완료 후 응답데이터 캐싱
        //    1-2) 있는 경우 캐싱된 데이터 받아서 응답
        return null;
    }
}
