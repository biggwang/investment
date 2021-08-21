package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.ProductDTO;
import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import com.kakaopay.ryuyungwang.investment.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final ProductService productService;
    private final InvestmentStatusService investmentStatusService;
    private final InvestmentRepository investmentRepository;

    /**
     * TODO
     * - 오류발생시 원복되어야 한다.
     * - redis 장애시 어떻게 할것인가
     * - rdb 장애시 어떻게 할것인가
     */
    // TODO cache 어노테이션 활용하기
    public InvestmentResultResponseDTO invest(InvestmentRequestDTO investmentRequestDTO) {
        try {
            if (investmentStatusService.isImPossibleInvestment(investmentRequestDTO.getProductId())) {
                return InvestmentResultResponseDTO.builder()
                        .investResultEnum(InvestResultEnum.SOLDOUT)
                        .build();
            }
            // 투자내역 데이터 영속성은 필요함 캐쉬는 서버 내렸다 올리면 휘발됨..
            investmentRepository.save(
                    InvestmentEntity.builder()
                            .productId(investmentRequestDTO.getProductId())
                            .userId(investmentRequestDTO.getUserId())
                            .investmentAmount(investmentRequestDTO.getInvestmentAmount())
                            .build()
            );
            // TODO 레디스에 투자 통계값을 먼저 저장하되 spring event로 db 저장은 따로하자
            investmentStatusService.applyStatus(investmentRequestDTO.getProductId(), investmentRequestDTO.getUserId(), investmentRequestDTO.getInvestmentAmount());
        } catch (Exception ex) {
            // TODO redis rollback 처리 및 rdb rollback 되는지 확인
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
        return investmentRepository.findByUserIdOrderByCreateAtDesc(userId)
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
                .investmentAt(investmentEntity.getCreateAt())
                .build();
    }
}
