package ac.su.kdt.redistrcontrol.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class RedisServiceTest {
    @Autowired
    RedisService redisService;
    @Test
    void setIfAbsent() {
        assert redisService.setIfAbsent(
                // 10초 동안 중복 키에 입력 금지 보장
                // 발생 시간 적기
                "hahaha", LocalDateTime.now().toString()
        );
    }
}