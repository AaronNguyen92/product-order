package com.dat.productorder.service;

import com.dat.productorder.dto.request.category.CreateCategoryRequest;
import com.dat.productorder.dto.request.category.UpdateCategoryRequest;
import com.dat.productorder.dto.response.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);

    void deleteCategory(Long id);
}
