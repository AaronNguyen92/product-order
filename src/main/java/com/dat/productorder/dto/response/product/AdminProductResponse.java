package com.dat.productorder.dto.response.product;

import com.dat.productorder.dto.response.category.CategorySummaryResponse;
import com.dat.productorder.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private ProductStatus status;

    private String imageUrl;

    private CategorySummaryResponse category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
