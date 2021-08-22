package com.kakaopay.ryuyungwang.investment.dto;

import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private Integer productId;

    private String title;

    private Integer totalInvestingAmount;

    private Integer currentInvestingAmount;

    private Integer totalInvestorCount;

    private ProductStatusEnum status;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

}
