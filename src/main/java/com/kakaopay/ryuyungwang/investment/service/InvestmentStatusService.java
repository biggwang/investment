package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.exception.ProductException;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.PROJECT_STATUS_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentStatusService {

    private final ProductRepository productRepository;
    private final InvestmentStatusQueryService investmentStatusQueryService;

    @Qualifier("investStatusStoreMysqlServiceImpl") @Autowired
    private InvestStatusStoreService investStatusStoreMysqlServiceImpl;

    @Qualifier("investStatusStoreRedisServiceImpl") @Autowired
    private InvestStatusStoreService investStatusStoreRedisServiceImpl;

    public boolean isImpossibleInvestment(Integer productId) {
       return investmentStatusQueryService.isImpossibleInvestment(productId);
    }

    public void applyStatus(Integer productId, Integer userId, Integer investAmount) {
        increaseInvestorCount(productId, userId);
        increaseCurrentInvestingAmount(productId, investAmount);
    }

    private void increaseInvestorCount(Integer productId, Integer userId) {
        Integer totalInvestorCount = investStatusStoreRedisServiceImpl.saveInvestorCount(productId, userId);
        Integer saveInvestorCount = investStatusStoreMysqlServiceImpl.saveInvestorCount(productId, totalInvestorCount);
        if (saveInvestorCount != 1) {
            investStatusStoreRedisServiceImpl.restoreInvestorCount(productId, userId);
        }
    }

    private void increaseCurrentInvestingAmount(Integer productId, Integer investAmount) {
        Long currentInvestmentAmount = investStatusStoreRedisServiceImpl.saveInvestmentAmount(productId, Long.valueOf(investAmount));
        Long saveResult = investStatusStoreMysqlServiceImpl.saveInvestmentAmount(productId, currentInvestmentAmount);
        if (saveResult == -1) {
            investStatusStoreRedisServiceImpl.restoreInvestmentAmount(productId, Long.valueOf(investAmount));
        }
    }

    // 해당 투자상품에 대한 투자자 수 조회
    public Integer getTotalInvestorCount(Integer productId) {
        return investmentStatusQueryService.getTotalInvestorCount(productId);
    }

    // 해당 투자상품 모집 금액
    public Integer getCurrentInvestingAmount(Integer productId) {
        return investmentStatusQueryService.getCurrentInvestingAmount(productId);
    }

    // 투자상품 상태값 변경
    @Cacheable(value = "changeProductStatus")
    public void changeProductStatus(Integer productId, ProductStatusEnum productStatusEnum) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        productEntity.setStatus(productStatusEnum);
        productRepository.save(productEntity);
    }
}
