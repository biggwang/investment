package com.kakaopay.ryuyungwang.investment.code;

import lombok.Getter;

@Getter
public enum InvestResultEnum {

    SUCCESS("성공적으로 투자되었습니다."),
    FAIL("투자가 실패하였습니다."),
    SOLDOUT("목표치 금액을 채웠습니다."),
    READ("데이터가 조회 되었습니다."),
    NOT_INVESTMENT("투자한 내역이 존재하지 않습니다."),
    NOT_EXIST_PRODUCT("존재하지 않는 상품입니다.");

    private final String message;

    InvestResultEnum(String message) {
        this.message = message;
    }
}
