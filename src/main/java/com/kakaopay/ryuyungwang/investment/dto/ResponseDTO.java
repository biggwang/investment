package com.kakaopay.ryuyungwang.investment.dto;

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
public class ResponseDTO<T> {

    private T data;

    @Builder.Default
    private boolean success = false;

    @Builder.Default
    private String message = "";

}
