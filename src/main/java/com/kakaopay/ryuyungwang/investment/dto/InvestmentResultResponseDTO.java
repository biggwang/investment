package com.kakaopay.ryuyungwang.investment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentResultResponseDTO {

    private Integer userId;

    private Integer productId;

    private Integer investmentAmount;

    private String result;

    @Builder
    public InvestmentResultResponseDTO(Integer userId, Integer productId, Integer investmentAmount, String result) {
        this.userId = userId;
        this.productId = productId;
        this.investmentAmount = investmentAmount;
        this.result = result;
    }
}
