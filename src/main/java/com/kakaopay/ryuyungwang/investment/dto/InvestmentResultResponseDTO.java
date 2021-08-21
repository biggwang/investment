package com.kakaopay.ryuyungwang.investment.dto;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentResultResponseDTO {

    @Builder.Default
    private Integer userId = 0;

    @Builder.Default
    private Integer productId = 0;

    @Builder.Default
    private Integer investmentAmount = 0;

    private InvestResultEnum investResultEnum;

}
