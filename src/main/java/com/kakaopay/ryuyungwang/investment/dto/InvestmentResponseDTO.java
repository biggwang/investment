package com.kakaopay.ryuyungwang.investment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class InvestmentResponseDTO {

    @ApiModelProperty(value = "투자상품 식별 아이디 값", example = "1")
    private Integer productId;

    @ApiModelProperty(value = "투자상품", example = "부동산 포트폴리오")
    private String title;

    @ApiModelProperty(value = "총 투자 모집금액", example = "7800000")
    private Integer totalInvestmentAmount;

    @ApiModelProperty(value = "현재 투자 모집금액", example = "24000")
    private Integer investmentAmount;

    @ApiModelProperty(value = "현재 투자자 수", example = "34")
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
