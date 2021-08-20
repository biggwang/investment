package com.kakaopay.ryuyungwang.investment.controller;

import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResultResponseDTO;
import com.kakaopay.ryuyungwang.investment.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 * - validation check
 */
@RestController
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;

    @PostMapping("/investment")
    public InvestmentResultResponseDTO invest(
            @RequestHeader("X-USER-ID") Integer userId,
            @RequestBody InvestmentRequestDTO investmentRequestDTO) {
        investmentRequestDTO.setUserId(userId);
        return investmentService.invest(investmentRequestDTO);
    }

    @PostMapping("/investment/me")
    public List<InvestmentResponseDTO> getInvestmentList(@RequestHeader("X-USER-ID") Integer userId) {
        return investmentService.getInvestmentList(userId);
    }
}
