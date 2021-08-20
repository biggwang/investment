package com.kakaopay.ryuyungwang.investment.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class RedisOperationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private int total = 0;

    @BeforeEach
    void setup() {
        redisTemplate.keys(TOTAL_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(CURRENT_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(INVESTOR_COUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
    }

    @Test
    @DisplayName("같은상품의 동일 투자자가 여러번 투자해도 투자 카운트는 1번만 되어야 한다")
    void opsForHashTest() {
        String key = "p1";
        String hashKey1 = "홍길동";
        String hashKey2 = "가길동";
        String hashKey3 = "나길동";
        String value1 = String.format("%s_%s", key, hashKey1);
        String value2 = String.format("%s_%s", key, hashKey2);
        String value3 = String.format("%s_%s", key, hashKey3);

        redisTemplate.opsForHash().put(key, hashKey1, value1);
        redisTemplate.opsForHash().put(key, hashKey1, value1);
        redisTemplate.opsForHash().put(key, hashKey1, value1);

        redisTemplate.opsForHash().put(key, hashKey2, value2);
        redisTemplate.opsForHash().put(key, hashKey2, value2);

        redisTemplate.opsForHash().put(key, hashKey3, value3);

        Integer size = redisTemplate.opsForHash().entries(key).size();
        assertEquals(size, 3);
    }

    @Test
    @DisplayName("투자금액 반영 테스트")
    void incrementTest() {
        String key = String.format("%s%s", "TEMP", "1");
        redisTemplate.opsForValue().increment(key, 100);
        redisTemplate.opsForValue().increment(key, 300);
        redisTemplate.opsForValue().increment(key, 5000);
        redisTemplate.opsForValue().increment(key, 7000);
        String value = redisTemplate.opsForValue().get(key);
        log.warn("##### result:{}", value);
        assertEquals(value, "12400");
    }


    @Test
    public void temp() {
        Random random = new Random();
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1, 10).forEach(i -> {
            service.execute(() -> {
                int num = random.nextInt(10);
                log.warn("################## num: {}", num);
                redisTemplate.opsForValue().increment("total", num);
            });
        });
        String result = redisTemplate.opsForValue().get("total");
        log.warn("################## result: {}", result);
    }

    @Test
    public void temp2() throws InterruptedException {
        Random random = new Random();
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0; i < 10; i++) {
            service.execute(() -> {
                latch.countDown();
                int num = random.nextInt(10);
                System.out.println("################ num:" + num);
                total += num;
                redisTemplate.opsForValue().increment("total", num);
            });
        }
        latch.await();
        log.warn("################## normal result: {}", total);;
        log.warn("################## redis result: {}", redisTemplate.opsForValue().get("total"));;
    }

}
