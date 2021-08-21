package com.kakaopay.ryuyungwang.investment.dto;

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
public class ResponseDTO<T> {

    private T data;

    @ApiModelProperty(value = "요청성공여부", example = "true")
    @Builder.Default
    private boolean success = false;

    @ApiModelProperty(value = "요청결과 메세지", example = "정상처리 되었습니다.")
    @Builder.Default
    private String message = "";

}
