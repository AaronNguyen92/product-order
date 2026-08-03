package com.dat.productorder.mapper;

import com.dat.productorder.dto.response.category.CategorySummaryResponse;
import com.dat.productorder.dto.response.product.ProductDetailResponse;
import com.dat.productorder.dto.response.product.AdminProductResponse;
import com.dat.productorder.dto.request.product.CreateProductRequest;
import com.dat.productorder.dto.response.product.ProductSummaryResponse;
import com.dat.productorder.entity.Category;
import com.dat.productorder.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(CreateProductRequest request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        return product;

    }

    public AdminProductResponse toResponse(Product product) {

        AdminProductResponse response = new AdminProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setStatus(product.getStatus());
        response.setImageUrl(product.getImageUrl());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        response.setCategory(toCategorySummaryResponse(product.getCategory()));

        return response;

    }

    private CategorySummaryResponse toCategorySummaryResponse(Category category) {

        CategorySummaryResponse response = new CategorySummaryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;

    }

    public ProductSummaryResponse toSummaryResponse(Product product) {

        return ProductSummaryResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .category(toCategorySummaryResponse(product.getCategory()))
                .build();

    }

    public ProductDetailResponse toDetailResponse(Product product) {

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .category(toCategorySummaryResponse(product.getCategory()))
                .build();

    }

}
