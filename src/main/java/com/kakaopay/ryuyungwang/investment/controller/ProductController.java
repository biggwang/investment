package com.kakaopay.ryuyungwang.investment.controller;

import com.kakaopay.ryuyungwang.investment.dto.ProductResponseDTO;
import com.kakaopay.ryuyungwang.investment.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductResponseDTO> getProductList() {
        return productService.getProductList();
    }
}
