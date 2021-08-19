package com.kakaopay.ryuyungwang.investment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentResponseDTO {

    private Integer productId;

    private String title;

    private Integer totalInvestmentAmount;

    private Integer investmentAmount;

    private LocalDateTime investmentAt;

    @Builder
    public InvestmentResponseDTO(Integer productId, String title, Integer totalInvestmentAmount, Integer investmentAmount, LocalDateTime investmentAt) {
        this.productId = productId;
        this.title = title;
        this.totalInvestmentAmount = totalInvestmentAmount;
        this.investmentAmount = investmentAmount;
        this.investmentAt = investmentAt;
    }
}
