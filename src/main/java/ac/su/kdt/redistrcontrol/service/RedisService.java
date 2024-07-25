package ac.su.kdt.redistrcontrol.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplateDb0;
    private final StringRedisTemplate redisTemplateDb1;
    private final StringRedisTemplate redisTemplateDb2;

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
}
