package ac.su.kdt.redistrcontrol.service;

import ac.su.kdt.redistrcontrol.domain.Product;
import ac.su.kdt.redistrcontrol.domain.ProductForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplateDb0;
    private final StringRedisTemplate redisTemplateDb1;
    private final StringRedisTemplate redisTemplateDb2;
    private final ObjectMapper objectMapper;

    public boolean setProduct(String transactionKey, Product product) {
        try {
            String productJsonString = objectMapper.writeValueAsString(product);
            return Boolean.TRUE.equals(
                    redisTemplateDb0.opsForValue().setIfAbsent(
                    transactionKey,
                    productJsonString
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getProduct(String transactionKey) {
        String productJsonString = redisTemplateDb0.opsForValue().get(transactionKey);
        try {
            return objectMapper.readValue(productJsonString, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Transaction 처리를 위한 메서드
    public boolean setIfAbsent(String key, String value) {
        return Boolean.TRUE.equals( // Boolean 가능. 왜? class 가 null 가능.
                redisTemplateDb0.opsForValue().setIfAbsent(
                        // 10초 동안 중복 키에 입력 금지 보장
                    key,
                    value,  // 사용 시나리오에 따라서 키 값을 아무 값이나 쓰지 않고 실제 사용되는 값으로 적용
                    Duration.ofSeconds(10)  // 사용 시나리에 따라
                )
        );
    }

    // TODO : ProductTransactionService 로 이동
    // DB 캐싱을 겸하는 Transaction 검사용 메서드
    public Optional<Product> setIfAbsentGetIfPresent(String key, Product value) {
        // 1) Object to JSON String 언파싱
        boolean isSet = Boolean.TRUE.equals(    // null 값까지 핸들링
            redisTemplateDb0.opsForValue().setIfAbsent(
                    key,
                    "",
                    Duration.ofSeconds(10)
            )
        );
        if (isSet) {
            // 캐시에서 받아온 값을 응답 (String -> Product 파싱)
            // 2) JSON String to Object 파싱
            String productJson = redisTemplateDb1.opsForValue().get(key);
            Product cacheProduct;
            return Optional.of(
                    new Product(
                            // Create 에 해당하는 데이터 채워주기
                    )
            );  // Product 객체 형태로 변환
        }
        // 현재 요청이 최초 요청이므로
        redisTemplateDb0.opsForValue().get(key);
        return Optional.empty();
    }
}
