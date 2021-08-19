package com.kakaopay.ryuyungwang.investment.exception;

import com.kakaopay.ryuyungwang.investment.code.InvestResponseEnum;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException{

    private final InvestResponseEnum investResponseEnum;

    public ProductException(InvestResponseEnum investResponseEnum) {
        super(investResponseEnum.getMessage());
        this.investResponseEnum = investResponseEnum;
    }
}
