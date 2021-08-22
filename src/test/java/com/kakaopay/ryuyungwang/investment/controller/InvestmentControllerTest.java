package com.kakaopay.ryuyungwang.investment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.ryuyungwang.investment.InvestmentApplication;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentRequestDTO;
import com.kakaopay.ryuyungwang.investment.dto.InvestmentResponseDTO;
import com.kakaopay.ryuyungwang.investment.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = InvestmentApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InvestmentControllerTest {

    @Autowired
    private WebApplicationContext ctx;
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();
    }

    @DisplayName("상품 투자하기 API 테스트")
    @MethodSource("parametersProvider")
    @ParameterizedTest(name = "productId:{1} userId:{2} investmentAmount:{3}")
    public void investTest(Integer productId, Integer userId, Integer investmentAmount, boolean result) throws Exception {
        // given
        InvestmentRequestDTO requestDTO = new InvestmentRequestDTO();
        requestDTO.setProductId(productId);
        requestDTO.setInvestmentAmount(investmentAmount);
        String bodyString = objectMapper.writeValueAsString(requestDTO);
        // when
        MvcResult mvcResult = mockMvc.perform(post("/investment")
                .header("X-USER-ID", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bodyString))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ResponseDTO<InvestmentRequestDTO> responseV1 = objectMapper.readValue(content, ResponseDTO.class);
        // then
        assertEquals(responseV1.isSuccess(), result);
    }

    private static Stream<Arguments> parametersProvider() {
        Integer productId = 4;
        return Stream.of(
                arguments(null, productId, 1000, false),
                arguments(productId, 0, 1000, false),
                arguments(productId, 1, null, false),
                arguments(productId, 1, 0, false),
                arguments(productId, 1, 1000, true)
        );
    }

    @DisplayName("내가 투자한 상품 조회하기 API 테스트")
    @MethodSource("getInvestProvider")
    @ParameterizedTest(name = "userId:{1} investmentAmount:{2}")
    public void getInvest(Integer userId, boolean result) throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/investment")
                .header("X-USER-ID", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ResponseDTO<List<InvestmentResponseDTO>> responseV1 = objectMapper.readValue(content, ResponseDTO.class);
        log.warn("### response: {}", responseV1);
        // then
        assertEquals(responseV1.isSuccess(), result);
    }

    private static Stream<Arguments> getInvestProvider() {
        Integer productId = 4;
        return Stream.of(
                arguments(productId, true)
        );
    }
}