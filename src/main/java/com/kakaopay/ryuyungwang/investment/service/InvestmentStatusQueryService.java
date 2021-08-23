package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.exception.ProductException;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentStatusQueryService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ProductRepository productRepository;

    public boolean isImpossibleInvestment(Integer productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        if (ProductStatusEnum.FINISHED.equals(productEntity.getStatus())) {
            return true;
        }
        String key = getCurrentInvestingAmountKey(productId);
        String currentInvestingAmountString = redisTemplate.opsForValue().get(key);
        Integer currentInvestingAmount = StringUtils.isEmpty(currentInvestingAmountString) ? 0 : Integer.parseInt(currentInvestingAmountString);
        Integer totalInvestingAmount = getTotalInvestingAmount(productId);
        log.warn("####### currentInvestingAmount:{}, totalInvestingAmount:{}", currentInvestingAmount, totalInvestingAmount);
        return currentInvestingAmount >= totalInvestingAmount;
    }


    // 해당 투자상품에 대한 투자자 수 조회
    public Integer getTotalInvestorCount(Integer productId) {
        String key = getTotalInvestorCountKey(productId);
        Integer totalInvestorCount = redisTemplate.opsForHash().entries(key).size();
        if (ObjectUtils.isEmpty(totalInvestorCount)) {
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
            return productEntity.getTotalInvestor();
        }
        return redisTemplate.opsForHash().entries(key).size();
    }

    private String getTotalInvestorCountKey(Integer productId) {
        return String.format("%s%s", INVESTOR_COUNT_PREFIX, productId);
    }

    public Integer getTotalInvestingAmount(Integer productId) {
        String key = getTotalInvestingAmountKey(productId);
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(value)) {
            return Integer.parseInt(value);
        }
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        Integer totalInvestingAmount = productEntity.getTotalInvestingAmount();
        redisTemplate.opsForValue().set(key, String.valueOf(totalInvestingAmount));
        return totalInvestingAmount;
    }

    private String getTotalInvestingAmountKey(Integer productId) {
        return String.format("%s%s", TOTAL_INVESTING_AMOUNT_PREFIX, productId);
    }

    // 해당 투자상품 모집 금액
    public Integer getCurrentInvestingAmount(Integer productId) {
        String key = getCurrentInvestingAmountKey(productId);
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
            return productEntity.getCurrentInvestingAmount();
        }
        return Integer.parseInt(value);
    }

    private String getCurrentInvestingAmountKey(Integer productId) {
        return String.format("%s%s", CURRENT_INVESTING_AMOUNT_PREFIX, productId);
    }
}
