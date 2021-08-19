package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResponseEnum;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.exception.ProductException;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;

@Service
@RequiredArgsConstructor
public class InvestmentStatusService {

    private final ProductRepository productRepository;
    private final InvestmentRepository investmentRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public boolean isImPossibleInvestment(Integer productId) {
        String key = String.format("%s%s", TOTAL_INVESTING_AMOUNT_PREFIX, productId);
        String currentInvestingAmountString = redisTemplate.opsForValue().get(key);
        Integer currentInvestingAmount = StringUtils.isEmpty(currentInvestingAmountString) ? 0 : Integer.parseInt(currentInvestingAmountString);
        Integer totalInvestingAmount = getTotalInvestingAmount(productId);
        return currentInvestingAmount >= totalInvestingAmount;
    }

    public Integer getTotalInvestingAmount(Integer productId) {
        String key = String.format("%s%s", TOTAL_INVESTING_AMOUNT_PREFIX, productId);
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(value)) {
            return Integer.parseInt(value);
        }
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResponseEnum.NOT_EXIST_PRODUCT));
        Integer totalInvestingAmount = productEntity.getTotalInvestingAmount();
        redisTemplate.opsForValue().set(key, String.valueOf(totalInvestingAmount));
        return totalInvestingAmount;
    }

    public Integer getTotalInvestorCount(Integer productId) {
        // TODO 데이터 없을경우 DB 조회
        String key = String.format("%s%s", INVESTOR_COUNT_PREFIX, productId);
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            return NumberUtils.INTEGER_ZERO;
        }
        return Integer.parseInt(value);
    }

    public Integer getCurrentInvestingAmount(Integer productId) {
        String key = String.format("%s%s", TOTAL_INVESTING_AMOUNT_PREFIX, productId);
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            // TODO DB 조회 고려
            return NumberUtils.INTEGER_ZERO;
        }
        return Integer.parseInt(value);
    }

    public void increaseCurrentInvestingAmount(Integer productId, Integer investAmount) {
        redisTemplate.opsForValue().increment(String.valueOf(productId), investAmount);
    }

    public void increaseInvestorCount(Integer productId, Integer userId) {
        boolean isInvested = investmentRepository.existsByProductIdAndUserId(productId, userId);
        if (isInvested) {
            return;
        }
        // TODO DB 후처리
        redisTemplate.opsForValue().increment(String.format("%s%s", INVESTOR_COUNT_PREFIX, productId));
    }
}
