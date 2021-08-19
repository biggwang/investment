package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.dto.ProductResponseDTO;
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

    public List<ProductResponseDTO> getProductList() {
        LocalDateTime now = LocalDateTime.now();
        return productRepository.findAllByStartedAtLessThanEqualAndFinishedAtGreaterThanEqual(now, now)
                .stream()
                .map(this::convertDto)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO convertDto(ProductEntity productEntity) {
        return ProductResponseDTO.builder()
                .productId(productEntity.getProductId())
                .title(productEntity.getTitle())
                .totalInvestingAmount(productEntity.getTotalInvestingAmount())
                .currentInvestingAmount(null) // TODO
                .totalInvestAmount(null)    // TODO
                .status(productEntity.getStatus())
                .startedAt(productEntity.getStartedAt())
                .finishedAt(productEntity.getFinishedAt())
                .build();
    }
}
