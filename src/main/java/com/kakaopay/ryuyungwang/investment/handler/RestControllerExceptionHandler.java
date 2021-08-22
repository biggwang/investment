package com.kakaopay.ryuyungwang.investment.handler;

import com.kakaopay.ryuyungwang.investment.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<String> serverException(Exception ex) {
        log.error("failed to error because {}", ex.getMessage());
        return ResponseDTO.<String>builder()
                .message("문제가 발생하였습니다. 잠시후에 다시 이용해주세요.")
                .data(StringUtils.EMPTY)
                .build();
    }
}
