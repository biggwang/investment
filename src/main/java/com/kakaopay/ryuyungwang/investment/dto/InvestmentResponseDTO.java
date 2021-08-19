package com.kakaopay.ryuyungwang.investment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvestmentResponseDTO {

    private String result;

    @Builder
    public InvestmentResponseDTO(String result) {
        this.result = result;
    }
}
