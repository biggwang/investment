package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.InvestmentApplication;
import com.kakaopay.ryuyungwang.investment.code.Constant;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
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

import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Slf4j
@SpringBootTest(classes = InvestmentApplication.class)
class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private InvestmentRepository investmentRepository;
    private int total = 0;

    @BeforeEach
    void setup() {
        investmentRepository.deleteAll();
    }

    @Test
    public void temp() {
        Random random = new Random();
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1, 10).forEach(i -> {
            service.execute(() -> {
                int num = random.nextInt(10);
//                log.warn("################## num: {}", num);
                System.out.println("################ num:" + num);
                redisTemplate.opsForValue().increment("total", num);
            });
        });
        String result = redisTemplate.opsForValue().get("total");
//        log.warn("################## result: {}", result);
        System.out.println("################ redis result:" + redisTemplate.opsForValue().get("total"));
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
        //log.warn("################## result: {}", total);;
        System.out.println("################ normal result:" + total);
        System.out.println("################ redis result:" + redisTemplate.opsForValue().get("total"));
    }

    @Test
    @DisplayName("투자하기 테스트")
    void investment() {
        InvestmentRequestDTO investmentRequestDTO = new InvestmentRequestDTO();
        investmentRequestDTO.setProductId(1);
        investmentRequestDTO.setUserId(123);
        investmentRequestDTO.setInvestmentAmount(1000);
        investmentService.invest(investmentRequestDTO);
        String key = String.format("%s%s", INVESTOR_COUNT_PREFIX, investmentRequestDTO.getProductId());
        String value = redisTemplate.opsForValue().get(key);
        assertEquals(value, "1");
    }
}