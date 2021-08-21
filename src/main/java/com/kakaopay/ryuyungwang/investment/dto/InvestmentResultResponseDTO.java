package com.kakaopay.ryuyungwang.investment.dto;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "사용자 식별 아이디 값", example = "345")
    @Builder.Default
    private Integer userId = 0;

    @ApiModelProperty(value = "투자상품 식별 아이디 값", example = "1")
    @Builder.Default
    private Integer productId = 0;

    @ApiModelProperty(value = "투자금액", example = "3000")
    @Builder.Default
    private Integer investmentAmount = 0;

    @ApiModelProperty(hidden = true)
    private InvestResultEnum investResultEnum;

}
