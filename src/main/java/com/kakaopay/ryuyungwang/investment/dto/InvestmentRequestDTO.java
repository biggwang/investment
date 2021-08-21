package com.kakaopay.ryuyungwang.investment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentRequestDTO {

    private Integer userId;

    @NotNull(message = "상품식별 아이디 값은 필숫값 입니다.")
    @Min(value = 1, message = "상품식별 아이디 값은 0보다 커야 합니다.")
    private Integer productId;

    @NotNull(message = "투자금액 값은 필숫값 입니다.")
    @Min(value = 1, message = "상품식별 아이디 값은 0보다 커야 합니다.")
    private Integer investmentAmount;
}
