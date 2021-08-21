package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.dto.ProductDTO;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.exception.ProductException;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InvestmentStatusService investmentStatusService;

    public List<ProductDTO> getProductList() {
        LocalDateTime now = LocalDateTime.now();
        return productRepository.findAllByStartedAtLessThanEqualAndFinishedAtGreaterThanEqual(now, now)
                .stream()
                .map(this::convertDto)
                .collect(Collectors.toList());
    }

    public ProductDTO getProduct(Integer productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(InvestResultEnum.NOT_EXIST_PRODUCT));
        return convertDto(productEntity);
    }

    private ProductDTO convertDto(ProductEntity productEntity) {
        Integer productId = productEntity.getProductId();
        return ProductDTO.builder()
                .productId(productId)
                .title(productEntity.getTitle())
                .totalInvestingAmount(productEntity.getTotalInvestingAmount())
                .currentInvestingAmount(investmentStatusService.getCurrentInvestingAmount(productId))
                .totalInvestorCount(investmentStatusService.getTotalInvestorCount(productId))
                .status(productEntity.getStatus())
                .startedAt(productEntity.getStartedAt())
                .finishedAt(productEntity.getFinishedAt())
                .build();
    }
}
