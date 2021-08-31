package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.ProductDTO;
import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final ProductService productService;
    private final InvestmentStatusService investmentStatusService;
    private final InvestmentRepository investmentRepository;

    @Transactional
    public InvestmentResultResponseDTO invest(InvestmentRequestDTO investmentRequestDTO) {
        try {
            if (investmentStatusService.isImpossibleInvestment(investmentRequestDTO.getProductId())) {
                investmentStatusService.changeProductStatus(investmentRequestDTO.getProductId(), ProductStatusEnum.FINISHED);
                return InvestmentResultResponseDTO.builder()
                        .productId(investmentRequestDTO.getProductId())
                        .userId(investmentRequestDTO.getUserId())
                        .investmentAmount(investmentRequestDTO.getInvestmentAmount())
                        .investResultEnum(InvestResultEnum.SOLDOUT)
                        .build();
            }
            investmentRepository.save(
                    InvestmentEntity.builder()
                            .productId(investmentRequestDTO.getProductId())
                            .userId(investmentRequestDTO.getUserId())
                            .investmentAmount(investmentRequestDTO.getInvestmentAmount())
                            .build()
            );
            investmentStatusService.applyStatus(investmentRequestDTO.getProductId(), investmentRequestDTO.getUserId(), investmentRequestDTO.getInvestmentAmount());
        } catch (Exception ex) {
            log.error("failed to invest because {}", ex.getMessage());
            return InvestmentResultResponseDTO.builder()
                    .investResultEnum(InvestResultEnum.FAIL)
                    .build();
        }
        return InvestmentResultResponseDTO.builder()
                .productId(investmentRequestDTO.getProductId())
                .userId(investmentRequestDTO.getUserId())
                .investmentAmount(investmentRequestDTO.getInvestmentAmount())
                .investResultEnum(InvestResultEnum.SUCCESS)
                .build();
    }

    public List<InvestmentResponseDTO> getInvestmentList(Integer userId) {
        return investmentRepository.findByUserIdOrderByInvestmentAtDesc(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private InvestmentResponseDTO toResponseDTO(InvestmentEntity investmentEntity) {
        ProductDTO productDTO = productService.getProduct(investmentEntity.getProductId());
        return InvestmentResponseDTO.builder()
                .productId(investmentEntity.getProductId())
                .title(productDTO.getTitle())
                .totalInvestmentAmount(productDTO.getTotalInvestingAmount())
                .investmentAmount(investmentEntity.getInvestmentAmount())
                .investmentAt(investmentEntity.getInvestmentAt())
                .build();
    }
}
