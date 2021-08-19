package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResponseEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;

    public InvestmentResponseDTO invest(@RequestBody InvestmentRequestDTO investmentRequestDTO) {
        // TODO 투자금액 초과 하지 않았는 체크
        boolean isPossibleInvestment = true;
        if (!isPossibleInvestment) {
            return InvestmentResponseDTO.builder()
                    .result(InvestResponseEnum.SOLDOUT.getMessage())
                    .build();
        }
        investmentRepository.save(
                InvestmentEntity.builder()
                        .productId(investmentRequestDTO.getProductId())
                        .userId(investmentRequestDTO.getUserId())
                        .investmentAmount(investmentRequestDTO.getInvestmentAmount())
                        .build()
        );

        // TODO 투자자수 count 처리 단, 같은 유저는 안됨

        return InvestmentResponseDTO.builder()
                .result(InvestResponseEnum.SUCCESS.getMessage())
                .build();
    }
}
