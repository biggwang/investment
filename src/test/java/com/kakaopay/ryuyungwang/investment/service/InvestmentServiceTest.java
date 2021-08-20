package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.InvestmentApplication;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@Slf4j
@SpringBootTest(classes = InvestmentApplication.class)
class InvestmentServiceTest {

    @Autowired
    private InvestmentService investmentService;
    @Autowired
    private InvestmentStatusService investmentStatusService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private InvestmentRepository investmentRepository;

    @BeforeEach
    void setup() {
        investmentRepository.deleteAll();
        redisTemplate.keys(TOTAL_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(CURRENT_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(INVESTOR_COUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
    }

    @Test
    @DisplayName("투자하기 테스트")
    void investment() {
        int productId = 1;
        int investmentAmount = 1000;
        InvestmentRequestDTO investmentRequestDTO = new InvestmentRequestDTO();
        investmentRequestDTO.setProductId(productId);
        investmentRequestDTO.setUserId(123);
        investmentRequestDTO.setInvestmentAmount(investmentAmount);
        investmentService.invest(investmentRequestDTO);

        Integer totalInvestorCount = investmentStatusService.getTotalInvestorCount(productId);
        assertEquals(totalInvestorCount, 1);
        Integer totalInvestingAmount = investmentStatusService.getCurrentInvestingAmount(productId);
        assertEquals(totalInvestingAmount, investmentAmount);
    }
}