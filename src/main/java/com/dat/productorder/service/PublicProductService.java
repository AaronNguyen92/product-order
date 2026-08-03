package com.dat.productorder.service;

import com.dat.productorder.dto.response.product.ProductDetailResponse;
import com.dat.productorder.dto.response.product.ProductSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicProductService {
    Page<ProductSummaryResponse> getProducts(
            String keyword,
            Long categoryId,
            Pageable pageable
    );

    ProductDetailResponse getProductById(Long id);
}
