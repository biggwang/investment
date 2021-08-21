package com.kakaopay.ryuyungwang.investment.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kakaopay.ryuyungwang.investment.code.InvestResultEnum;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.ResponseDTO;
import com.kakaopay.ryuyungwang.investment.exception.BadRequestException;
import com.kakaopay.ryuyungwang.investment.service.InvestmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

@Slf4j
@Validated
@RestController
@Api(tags = "INVESTMENT")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;
    private static final String USER_ID_INVALID_MESSAGE = "회원 아이디 값은 0보다 커야 합니다.";

    @PostMapping("/investment")
    @ApiOperation(value = "2.투자하기", notes = "총 투자 모집금액이 초과되면 SOLDOUT 되어 투자가 되지 않습니다.")
    @ApiImplicitParam(name = "X-USER-ID", value = "사용자 식별 아이디 값", example = "123", required = true, dataType = "int", paramType = "header")
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
    @ApiOperation(value = "3.나의 투자상품 조회", notes = "내가 투자한 상품 전체 내역을 볼 수 있습니다.")
    @ApiImplicitParam(name = "X-USER-ID", value = "사용자 식별 아이디 값", example = "123", required = true, dataType = "int", paramType = "header")
    public ResponseDTO<List<InvestmentResponseDTO>> getInvestmentList(
            @RequestHeader("X-USER-ID") @NotNull @Min(value = 1, message = USER_ID_INVALID_MESSAGE) Integer userId) {
        List<InvestmentResponseDTO> result = investmentService.getInvestmentList(userId);
        return ResponseDTO.<List<InvestmentResponseDTO>>builder()
                .success(true)
                .message(InvestResultEnum.READ.getMessage())
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
}
