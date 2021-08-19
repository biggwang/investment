package com.kakaopay.ryuyungwang.investment.code;

import lombok.Getter;

@Getter
public enum ProductStatusEnum {

    RECRUITMENT("모집중"),
    DEADLINE("마감");

    private final String description;

    ProductStatusEnum(String description) {
        this.description = description;
    }
}
