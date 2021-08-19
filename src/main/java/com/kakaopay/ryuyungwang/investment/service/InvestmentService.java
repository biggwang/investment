package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResponseEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentStatusService investmentStatusService;
    private final InvestmentRepository investmentRepository;

    public InvestmentResultResponseDTO invest(InvestmentRequestDTO investmentRequestDTO) {
        if (investmentStatusService.isImPossibleInvestment(investmentRequestDTO.getProductId())) {
            return InvestmentResultResponseDTO.builder()
                    .result(InvestResponseEnum.SOLDOUT.getMessage())
                    .build();
        }
        // TODO 메소드 위치 확인
        investmentStatusService.increaseInvestorCount(investmentRequestDTO.getProductId(), investmentRequestDTO.getUserId());
        investmentRepository.save(
                InvestmentEntity.builder()
                        .productId(investmentRequestDTO.getProductId())
                        .userId(investmentRequestDTO.getUserId())
                        .investmentAmount(investmentRequestDTO.getInvestmentAmount())
                        .build()
        );
        investmentStatusService.increaseCurrentInvestingAmount(investmentRequestDTO.getProductId(), investmentRequestDTO.getInvestmentAmount());
        return InvestmentResultResponseDTO.builder()
                .result(InvestResponseEnum.SUCCESS.getMessage())
                .build();
    }

    public List<InvestmentResponseDTO> getInvestmentList(Integer userId) {
        return investmentRepository.findByUserIdOrderByCreateAtDesc(userId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private InvestmentResponseDTO toResponseDTO(InvestmentEntity investmentEntity) {
        return InvestmentResponseDTO.builder()
                .productId(investmentEntity.getProductId())
                .title(null)
                .totalInvestmentAmount(null)
                .investmentAmount(investmentEntity.getInvestmentAmount())
                .investmentAt(investmentEntity.getCreateAt())
                .build();
    }
}
