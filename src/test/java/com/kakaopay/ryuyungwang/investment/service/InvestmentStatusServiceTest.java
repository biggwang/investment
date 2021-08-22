package com.kakaopay.ryuyungwang.investment.service;

import com.kakaopay.ryuyungwang.investment.InvestmentApplication;
import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import com.kakaopay.ryuyungwang.investment.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(classes = InvestmentApplication.class)
class InvestmentStatusServiceTest {

    @Autowired
    private InvestmentStatusService investmentStatusService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("동일한 파라미터로 모집 상품을 상태값을 업데이트 할 때는 캐시처리 하는지 테스트")
    void changeProductStatus() {
        // given
        Integer productId = 4;
        // when
        investmentStatusService.changeProductStatus(productId, ProductStatusEnum.FINISHED);
        investmentStatusService.changeProductStatus(productId, ProductStatusEnum.FINISHED);
        // then
        ProductEntity productEntity = productRepository.findById(productId).get();
        assertEquals(productEntity.getStatus(), ProductStatusEnum.FINISHED);
    }
}