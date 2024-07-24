package ac.su.kdt.redistrcontrol.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisServiceTest {
    @Autowired
    RedisService redisService;
    @Test
    void set() {
        assert redisService.setIfAbsent(
                "hahaha", "this is test!"
        );
    }
}