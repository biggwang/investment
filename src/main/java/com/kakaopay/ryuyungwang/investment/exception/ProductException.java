package com.kakaopay.ryuyungwang.investment.exception;

import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException{

    private final InvestResultEnum investResultEnum;

    public ProductException(InvestResultEnum investResultEnum) {
        super(investResultEnum.getMessage());
        this.investResultEnum = investResultEnum;
    }
}
