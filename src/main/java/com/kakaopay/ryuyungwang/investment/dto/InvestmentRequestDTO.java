package com.kakaopay.ryuyungwang.investment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentRequestDTO {

    private String userId;

    private Integer productId;

    private Integer investmentAmount;
}
