package com.kakaopay.ryuyungwang.investment.repository;

import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class InvestmentRepositoryTest {

    @Autowired
    private InvestmentRepository investmentRepository;

    @BeforeEach
    void setup() {
        investmentRepository.deleteAll();
    }

    @Test
    @DisplayName("투자 INSERT 테스트")
    void save() {
        investmentRepository.save(
                InvestmentEntity.builder()
                        .productId(1)
                        .userId(RandomStringUtils.randomNumeric(10))
                        .investmentAmount(Integer.parseInt(RandomStringUtils.randomNumeric(5)))
                        .build()
        );
    }

    @Test
    @DisplayName("내가 투자한 내역 조회 테스트")
    void getInvestmentList() {
        // given
        String randomUserId = RandomStringUtils.randomAlphabetic(10);
        investmentRepository.saveAll(
                Arrays.asList(
                        getInvestmentEntity(1, randomUserId, 1000),
                        getInvestmentEntity(1, randomUserId, 3000),
                        getInvestmentEntity(1, randomUserId, 5000)
                )
        );
        // when
        List<InvestmentEntity> list = investmentRepository.findByUserIdOrderByCreateAtDesc(randomUserId);
        //then
        assertEquals(list.size(), 3);
    }

    private InvestmentEntity getInvestmentEntity(Integer productId, String userId, Integer investmentAmount) {
        return InvestmentEntity.builder()
                .productId(productId)
                .userId(userId)
                .investmentAmount(investmentAmount)
                .build();
    }
}