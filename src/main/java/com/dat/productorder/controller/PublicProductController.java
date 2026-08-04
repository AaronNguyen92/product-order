package com.dat.productorder.controller;

import com.dat.productorder.dto.response.product.ProductDetailResponse;
import com.dat.productorder.dto.response.product.ProductSummaryResponse;
import com.dat.productorder.service.PublicProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Public Product",
        description = "APIs for displaying Product"
)
@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
public class PublicProductController {

    private final PublicProductService publicProductService;

    @Operation(summary = "Get Product List")
    @GetMapping
    public Page<ProductSummaryResponse> getProducts(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId, Pageable pageable) {

        return publicProductService.getProducts(keyword, categoryId, pageable);
    }


    @Operation(summary = "Get Product Detail")
    @GetMapping("/{id}")
    public ProductDetailResponse getProductById(@PathVariable Long id) {
        return publicProductService.getProductById(id);
    }
}
