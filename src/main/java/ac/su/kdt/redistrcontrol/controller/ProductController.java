package ac.su.kdt.redistrcontrol.controller;

import ac.su.kdt.redistrcontrol.domain.Product;
import ac.su.kdt.redistrcontrol.domain.ProductForm;
import ac.su.kdt.redistrcontrol.domain.ProductResponseDTO;
import ac.su.kdt.redistrcontrol.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        try {
//            List<Product> products = productService.getAllProducts();
//            return new ResponseEntity<>(products, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // 카테고리를 현재 Parent만 나옴.
    // 한 번에 가져오려면 CategoryController를 작성 후 가져와야함.
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            List<ProductResponseDTO> productResponseDTOS = ProductResponseDTO.fromProducts(products);
            return new ResponseEntity<>(productResponseDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.getProductById(id);
            if (product.isPresent()) {
                return new ResponseEntity<>(ProductResponseDTO.fromProduct(product.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping
//    public ResponseEntity<Product> createProduct(
//            @RequestBody Product product    // 수정 : Product 생성 시, Category 가 부산물로 함께 생성되는 것은 자연스럽지 못함.
//            // [과제] Redis Transaction 실제 로직 적용
//            // 1) ProductDTO 형태로 만들고 예제 호출 POSTMAN 생성
//            // 2) ProductTransactionController 를 만들고,
//            //    redis 에서 발급받은 키가 제출되어야만 후속 create 로직 수행하도록 엔드포인트 구현
//            // 3) 10초 범위를 따닥 및 중복 호출 방지하고자 하는 시간 구간에 맞추어서 조절 및 테스트 수행
//            //    => 프론트엔드 이벤트와 붙여서 테스트하면 가장 좋음
//    ) {
//        try {
//            Product createdProduct = productService.createProduct(product);
//            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    // 1) ProductDTO 형태로 만들고 예제 호출 POSTMAN 생성 + Category
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody ProductForm product
    ) {
        try {
            Product createdProduct = productService.createProduct(product.toEntity());
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Optional<Product> updatedProduct = productService.updateProduct(id, product);
            if (updatedProduct.isPresent()) {
                return new ResponseEntity<>(updatedProduct.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            boolean isDeleted = productService.deleteProduct(id);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}