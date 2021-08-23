package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.InvestmentApplication;
import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
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
    private final Integer productId = 4;

    @BeforeEach
    void setup() {
        investmentRepository.deleteAll();
        redisTemplate.keys(TOTAL_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(CURRENT_INVESTING_AMOUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
        redisTemplate.keys(INVESTOR_COUNT_PREFIX).forEach(key -> redisTemplate.delete(key));
    }

    @Test
    @DisplayName("투자하기 테스트")
    void investTest() {
        InvestmentResultResponseDTO investmentResultResponseDTO = invest(productId, 123, 1000);
        Integer totalInvestorCount = investmentStatusService.getTotalInvestorCount(investmentResultResponseDTO.getProductId());
        Integer totalInvestingAmount = investmentStatusService.getCurrentInvestingAmount(investmentResultResponseDTO.getProductId());
        assertEquals(totalInvestorCount, 1);
        assertEquals(totalInvestingAmount, investmentResultResponseDTO.getInvestmentAmount());
    }

    @Test
    @DisplayName("모집금액보다 투자금액이 많으면 SOLDOUT 처리 되어야 한다.")
    void investFailTest() {
        InvestmentResultResponseDTO resultFirst = invest(productId, 123, Integer.MAX_VALUE);
        InvestmentResultResponseDTO resultSecond = invest(productId, 123, 1000);
        assertEquals(resultFirst.getInvestResultEnum().getMessage(), InvestResultEnum.SUCCESS.getMessage());
        assertEquals(resultSecond.getInvestResultEnum().getMessage(), InvestResultEnum.SOLDOUT.getMessage());
    }

    @Test
    @DisplayName("동시에 투자하기 테스트")
    void multiInvestTest() throws InterruptedException {
        Random random = new Random();
        ExecutorService service = Executors.newFixedThreadPool(10);
        IntStream.rangeClosed(1, 10).boxed().parallel().forEach(i -> {
            invest(productId, 1, random.nextInt(5));
        });
        List<InvestmentResponseDTO> list = investmentService.getInvestmentList(1);
        log.warn("### result:{}", list);
    }

    private InvestmentResultResponseDTO invest(int productId, int userId, int investmentAmount) {
        InvestmentRequestDTO investmentRequestDTO = new InvestmentRequestDTO();
        investmentRequestDTO.setProductId(productId);
        investmentRequestDTO.setUserId(userId);
        investmentRequestDTO.setInvestmentAmount(investmentAmount);
        InvestmentResultResponseDTO result = investmentService.invest(investmentRequestDTO);
        log.warn("####### invest message: {}", result.getInvestResultEnum().getMessage());
        return result;
    }
}