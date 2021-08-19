package com.kakaopay.ryuyungwang.investment.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class InvestmentStatusServiceTest {

    @Autowired
    private InvestmentStatusService investmentStatusService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("increase 테스트")
    void increase() {
        redisTemplate.opsForValue().increment("temp");
        String result = redisTemplate.opsForValue().get("temp");
        assertEquals(result, "1");
    }

}