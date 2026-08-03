package com.dat.productorder.mapper;

import com.dat.productorder.dto.request.category.CreateCategoryRequest;
import com.dat.productorder.dto.request.category.UpdateCategoryRequest;
import com.dat.productorder.dto.response.category.CategoryResponse;
import com.dat.productorder.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();

    }

    public Category toEntity(CreateCategoryRequest request) {

        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

    }

    public void updateEntity(Category category, UpdateCategoryRequest request) {

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(request.getStatus());

    }

}
