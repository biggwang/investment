package com.kakaopay.ryuyungwang.investment.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.ResponseDTO;
import com.kakaopay.ryuyungwang.investment.exception.BadRequestException;
import com.kakaopay.ryuyungwang.investment.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO
 * - validation check
*/
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;
    private static final String USER_ID_INVALID_MESSAGE = "회원 아이디 값은 0보다 커야 합니다.";

    @PostMapping("/investment")
    public ResponseDTO<InvestmentResultResponseDTO> invest(
            @RequestHeader("X-USER-ID") @NotNull @Min(value = 1, message = USER_ID_INVALID_MESSAGE) Integer userId,
            @Valid @RequestBody InvestmentRequestDTO investmentRequestDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String message = bindingResult.getFieldError().getDefaultMessage();
            throw new BadRequestException(message);
        }

        investmentRequestDTO.setUserId(userId);
        InvestmentResultResponseDTO investResult = investmentService.invest(investmentRequestDTO);
        boolean success = InvestResultEnum.SUCCESS.equals(investResult.getInvestResultEnum());

        return ResponseDTO.<InvestmentResultResponseDTO>builder()
                .success(success)
                .message(investResult.getInvestResultEnum().getMessage())
                .data(investResult)
                .build();
    }

    @GetMapping("/investment/me")
    public ResponseDTO<List<InvestmentResponseDTO>> getInvestmentList(
            @RequestHeader("X-USER-ID") @NotNull @Min(value = 1, message = USER_ID_INVALID_MESSAGE) Integer userId) {
        List<InvestmentResponseDTO> result = investmentService.getInvestmentList(userId);
        return ResponseDTO.<List<InvestmentResponseDTO>>builder()
                .success(true)
                .message(InvestResultEnum.SUCCESS.getMessage())
                .data(result)
                .build();
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, InvalidFormatException.class, BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO badRequestException(Exception ex) {
        log.warn("bad request because {}", ex.getMessage());
        return ResponseDTO.builder()
                .message(ex.getMessage())
                .data(StringUtils.EMPTY)
                .build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO serverException(Exception ex) {
        log.error("failed to error because {}", ex.getMessage());
        return ResponseDTO.builder()
                .message("문제가 발생하였습니다. 잠시후에 다시 이용해주세요.")
                .data(StringUtils.EMPTY)
                .build();
    }

}
