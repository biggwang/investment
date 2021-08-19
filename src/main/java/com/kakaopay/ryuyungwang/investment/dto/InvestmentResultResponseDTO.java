package com.kakaopay.ryuyungwang.investment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentResultResponseDTO {

    private String result;

    @Builder
    public InvestmentResultResponseDTO(String result) {
        this.result = result;
    }
}
