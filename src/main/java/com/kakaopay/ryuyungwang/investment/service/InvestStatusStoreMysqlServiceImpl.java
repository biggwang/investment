package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.exception.ProductException;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kakaopay.ryuyungwang.investment.code.Constant.INVESTOR_COUNT_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestStatusStoreMysqlServiceImpl implements InvestStatusStoreService {

    private final ProductRepository productRepository;

    @Override
    public Long saveInvestmentAmount(Integer productId, Long investmentAmount) {
        try {
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
            productEntity.setCurrentInvestingAmount(investmentAmount.intValue());
            return Long.valueOf(productRepository.save(productEntity).getProductId());
        } catch (Exception e) {
            log.error("saveInvestmentAmount to mysql because {}", e.getMessage());
            return -1L;
        }
    }

    @Override
    public Long restoreInvestmentAmount(Integer productId, Long investmentAmount) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        productEntity.setCurrentInvestingAmount(-investmentAmount.intValue());
        return Long.valueOf(productRepository.save(productEntity).getProductId());
    }

    @Override
    public Integer saveInvestorCount(Integer productId, Integer totalInvestorCount) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        productEntity.setTotalInvestor(totalInvestorCount);
        return productRepository.save(productEntity).getProductId();
    }

    @Override
    public Integer restoreInvestorCount(Integer productId, Integer totalInvestorCount) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        productEntity.setTotalInvestor(-totalInvestorCount);
        return productRepository.save(productEntity).getProductId();
    }
}
