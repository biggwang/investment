package com.kakaopay.ryuyungwang.investment.controller;

import com.kakaopay.ryuyungwang.investment.dto.ProductDTO;
import com.kakaopay.ryuyungwang.investment.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "PRODUCT")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "1.전체 투자 상품 조회", notes = "상품 모집기간내의 상품만 조회 됩니다.")
    @GetMapping("/products")
    public List<ProductDTO> getProductList() {
        return productService.getProductList();
    }
}
