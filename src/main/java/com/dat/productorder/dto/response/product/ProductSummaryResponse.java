package com.dat.productorder.dto.response.product;

import com.dat.productorder.dto.response.category.CategorySummaryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSummaryResponse {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private CategorySummaryResponse category;
}
