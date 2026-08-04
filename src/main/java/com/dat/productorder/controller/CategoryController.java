package com.dat.productorder.controller;

import com.dat.productorder.dto.request.category.CreateCategoryRequest;
import com.dat.productorder.dto.request.category.UpdateCategoryRequest;
import com.dat.productorder.dto.response.category.CategoryResponse;
import com.dat.productorder.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Category Management",
        description = "APIs for managing Categories"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Admin -  Get Category List")
    @GetMapping
    public List<CategoryResponse> getAllCategories() {

        return categoryService.getAllCategories();

    }


    @Operation(summary = "Admin - Create Category")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory( @Valid @RequestBody CreateCategoryRequest request) {

        return categoryService.createCategory(request);

    }


    @Operation(summary = "Admin - Update Category")
    @PutMapping("/{id}")
    public CategoryResponse updateCategory( @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        return categoryService.updateCategory(id, request);

    }


    @Operation(summary = "Admin - Delete Category")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

    }
}
