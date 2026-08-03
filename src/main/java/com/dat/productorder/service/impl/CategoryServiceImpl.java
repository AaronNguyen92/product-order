package com.dat.productorder.service.impl;

import com.dat.productorder.dto.response.category.CategoryResponse;
import com.dat.productorder.entity.Category;
import com.dat.productorder.enums.CategoryStatus;
import com.dat.productorder.exception.ConflictException;
import com.dat.productorder.exception.DuplicateResourceException;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.mapper.CategoryMapper;
import com.dat.productorder.repository.CategoryRepository;
import com.dat.productorder.repository.ProductRepository;
import com.dat.productorder.service.CategoryService;
import com.dat.productorder.dto.request.category.CreateCategoryRequest;
import com.dat.productorder.dto.request.category.UpdateCategoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {

        //check duplicate
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateResourceException("Category name already exists.");
        }

        //map
        Category category = categoryMapper.toEntity(request);

        //ACTIVE default
        category.setStatus(CategoryStatus.ACTIVE);

        //save
        Category savedCategory = categoryRepository.save(category);

        log.info("Admin created category '{}'.", category.getName());

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {

        //find
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));

        //check duplicate
        if (categoryRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException("Category name already exists.");
        }

        //mapper.updateEntity()
        categoryMapper.updateEntity(category, request);

        //save
        Category savedCategory = categoryRepository.save(category);

        log.info("Admin updated category '{}'.", category.getName());

        return categoryMapper.toResponse(savedCategory);

    }

    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));

        if (productRepository.existsByCategoryId(category.getId())) {
            throw new ConflictException("Cannot delete category because it contains products.");
        }

        categoryRepository.delete(category);

        log.info("Admin deleted category '{}'.", category.getName());

    }
}
