package com.kakaopay.ryuyungwang.investment.repository;

import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        redisTemplate.keys(TOTAL_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(CURRENT_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(INVESTOR_COUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
    }

    @Test
    @DisplayName("상품 모집기간 내의 상품만 조회 되는지 테스트")
    void select() {
        // given
        LocalDateTime standardDate = LocalDateTime.of(2021, 8, 23, 0, 0, 0);
        productRepository.saveAll(
                Arrays.asList(
                        getProductEntity(
                                LocalDateTime.of(2019, 8, 19, 0, 0, 0),
                                LocalDateTime.of(2020, 8, 19, 0, 0, 0)
                        ),
                        getProductEntity(
                                LocalDateTime.of(2021, 8, 1, 0, 0, 0),
                                LocalDateTime.of(2021, 12, 31, 0, 0, 0)
                        )
                )
        );
        // when
        List<ProductEntity> list = productRepository.findAllByStartedAtLessThanEqualAndFinishedAtGreaterThanEqual(standardDate, standardDate);
        // then
        assertEquals(list.size(), 1);
    }

    private ProductEntity getProductEntity(LocalDateTime start, LocalDateTime end) {
        return ProductEntity.builder()
                .title(RandomStringUtils.randomAlphabetic(10))
                .totalInvestingAmount(1000)
                .status(ProductStatusEnum.RECRUITMENT)
                .startedAt(start)
                .finishedAt(end)
                .build();
    }

}