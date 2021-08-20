package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResponseEnum;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.exception.ProductException;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.TOTAL_INVESTING_AMOUNT_PREFIX;

@Service
@RequiredArgsConstructor
public class InvestmentStatusService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // TODO
    public boolean isImPossibleInvestment(Integer productId, Integer investAmount) {
        String key = getTotalInvestingAmountKey(productId);
        String currentInvestingAmountString = redisTemplate.opsForValue().get(key);
        Integer currentInvestingAmount = StringUtils.isEmpty(currentInvestingAmountString) ? 0 : Integer.parseInt(currentInvestingAmountString);
        Integer totalInvestingAmount = getTotalInvestingAmount(productId);
        return currentInvestingAmount >= totalInvestingAmount;
    }

    public void applyStatus(Integer productId, Integer userId, Integer investAmount) {
        increaseInvestorCount(productId, userId);
        increaseCurrentInvestingAmount(productId, investAmount);
    }

    // hashTable 형태를 사용한 이유는 table 형태로 관리하여 count를 한꺼번에 조회 하기 위함
    public void increaseInvestorCount(Integer productId, Integer userId) {
        String key = getTotalInvestorCountKey(productId);
        String hashKey = String.valueOf(userId);
        String hashValue = String.format("%s_%s", key, hashKey);
        redisTemplate.opsForHash().put(key, hashKey, hashValue);
    }

    public Integer getTotalInvestorCount(Integer productId) {
        String key = getTotalInvestorCountKey(productId);
        Integer totalInvestorCount = redisTemplate.opsForHash().entries(key).size();
        if (ObjectUtils.isEmpty(totalInvestorCount)) {
            // TODO DB 조회 고려
            return NumberUtils.INTEGER_ZERO;
        }
        return redisTemplate.opsForHash().entries(key).size();
    }

    // 상품 총 모집금액 조회
    public Integer getTotalInvestingAmount(Integer productId) {
        String key = getTotalInvestingAmountKey(productId);
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

    public void increaseCurrentInvestingAmount(Integer productId, Integer investAmount) {
        String key = getCurrentInvestingAmountKey(productId);
        redisTemplate.opsForValue().increment(key, investAmount);
    }

    // 현재 상품 모집 금액
    public Integer getCurrentInvestingAmount(Integer productId) {
        String key = getCurrentInvestingAmountKey(productId);
        String value = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(value)) {
            // TODO DB 조회 고려
            return NumberUtils.INTEGER_ZERO;
        }
        return Integer.parseInt(value);
    }

    private String getTotalInvestingAmountKey(Integer productId) {
        return String.format("%s%s", TOTAL_INVESTING_AMOUNT_PREFIX, productId);
    }

    private String getCurrentInvestingAmountKey(Integer productId) {
        return String.format("%s%s", CURRENT_INVESTING_AMOUNT_PREFIX, productId);
    }

    private String getTotalInvestorCountKey(Integer productId) {
        return String.format("%s%s", INVESTOR_COUNT_PREFIX, productId);
    }
}
