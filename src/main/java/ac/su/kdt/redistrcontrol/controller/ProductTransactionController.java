package ac.su.kdt.redistrcontrol.controller;

import ac.su.kdt.redistrcontrol.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-transactions")
public class ProductTransactionController {
    // redis에서 발급받은 키 제출되어야만 후속 create 로직 수행하도록 엔트포인트 구현
    private final RedisService redisService;

    @GetMapping
    public ResponseEntity<String> transactionResultTest(
            @RequestParam String key
    ) {
        boolean isTransactionSuccess = redisService.setIfAbsent(
                key, LocalDateTime.now().toString()
        );
        return new ResponseEntity<>(
                isTransactionSuccess ? "Transaction Success" : "Transaction Fail",
                isTransactionSuccess ? HttpStatus.OK : HttpStatus.CONFLICT
        );
    }
}
