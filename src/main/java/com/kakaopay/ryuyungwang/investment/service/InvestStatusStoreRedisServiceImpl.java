package com.kakaopay.ryuyungwang.investment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.kakaopay.ryuyungwang.investment.code.Constant.CURRENT_INVESTING_AMOUNT_PREFIX;
import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestStatusStoreRedisServiceImpl implements InvestStatusStoreService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Long saveInvestmentAmount(Integer productId, Long investmentAmount) {
        try {
            String key = getCurrentInvestingAmountKey(productId);
            return redisTemplate.opsForValue().increment(key, investmentAmount);
        } catch (Exception e) {
            log.error("saveInvestmentAmount to redis because {}", e.getMessage());
            return -1L;
        }
    }

    @Override
    public Long restoreInvestmentAmount(Integer productId, Long investmentAmount) {
        String key = getCurrentInvestingAmountKey(productId);
        return redisTemplate.opsForValue().decrement(key, investmentAmount);
    }

    private String getCurrentInvestingAmountKey(Integer productId) {
        return String.format("%s%s", CURRENT_INVESTING_AMOUNT_PREFIX, productId);
    }

    @Override
    public Integer saveInvestorCount(Integer productId, Integer userId) {
        try {
            String key = getTotalInvestorCountKey(productId);
            String hashKey = String.valueOf(userId);
            String hashValue = String.format("%s_%s", key, hashKey);
            redisTemplate.opsForHash().put(key, hashKey, hashValue);
            String value = redisTemplate.opsForValue().get(key);
            return ObjectUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
        } catch (Exception e) {
            log.error("saveInvestorCount to redis because {}", e.getMessage());
            return -1;
        }
    }

    @Override
    public Integer restoreInvestorCount(Integer productId, Integer userId) {
        String key = getTotalInvestorCountKey(productId);
        String hashKey = String.valueOf(userId);
        String hashValue = String.format("%s_%s", key, hashKey);
        redisTemplate.opsForHash().delete(key, hashKey, hashValue);
        String value = redisTemplate.opsForValue().get(key);
        return ObjectUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
    }

    private String getTotalInvestorCountKey(Integer productId) {
        return String.format("%s%s", INVESTOR_COUNT_PREFIX, productId);
    }
}
