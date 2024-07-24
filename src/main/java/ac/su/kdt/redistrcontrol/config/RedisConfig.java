package ac.su.kdt.redistrcontrol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {        // 메서드명이 Bean 의 이름이 됨.
        return new LettuceConnectionFactory(
                new RedisStandaloneConfiguration(
                        // 원격 이용 및 Docker 사용을 염두에 두고 full args 생성자 사용
                        redisHost, redisPort
                )
        );
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory0() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisConf.setDatabase(0);
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory1() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisConf.setDatabase(1);
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory2() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration(redisHost, redisPort);
        redisConf.setDatabase(1);
        return new LettuceConnectionFactory(redisConf);
    }

    // =================== Redis 접속객체 생성 완료 ===================
    // 커맨드 구조를 미리 정의하느 Template 객체를 사용해야 실제 Redis 명령어를 사용할 수 있음.

    @Bean   // 없앨 수 없음.
    public RedisTemplate<String, String> redisTemplate(     // 보일러 플레이트, 하나도 빼지말아야함!
            RedisConnectionFactory redisConnectionFactory
            // 보일러 플레이트 -> 메서드 시그니처를 따르는 메서드가 반드시 필요한 경우가 많음.
    ) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);     // good Pattern
        // redisTemplate 메서드 내에서 커넥션을 새롭게 생성
        // redisTemplate.setConnectionFactory(redisConnectionFactory());    - Anti-Pattern
        // Bean 으로 제작한 객체는 주입받아서 사용하자 -> 인자 부분 없애지 말기!
        // 파라미터 주입 방식으로 Bean을 받아서 Singleton 사용법을 준수
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate redisTemplateDb0() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory0());
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate redisTemplateDb1() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory1());
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate redisTemplateDb2() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory2());
        return redisTemplate;
    }
}
