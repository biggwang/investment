package com.kakaopay.ryuyungwang.investment.repository;

import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
//@RequiredArgsConstructor
class InvestmentRepositoryTest {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Test
    @DisplayName("투자 INSERT 테스트")
    void save() {
        investmentRepository.save(
                InvestmentEntity.builder()
                        .investmentId(Integer.parseInt(RandomStringUtils.randomNumeric(2)))
                        .productId(1)
                        .userId(RandomStringUtils.randomNumeric(10))
                        .investmentAmount(Integer.parseInt(RandomStringUtils.randomNumeric(5)))
                        .build()
        );
    }
}