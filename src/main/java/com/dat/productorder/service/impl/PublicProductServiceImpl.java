package com.dat.productorder.service.impl;

import com.dat.productorder.dto.response.product.ProductDetailResponse;
import com.dat.productorder.dto.response.product.ProductSummaryResponse;
import com.dat.productorder.entity.Product;
import com.dat.productorder.enums.CategoryStatus;
import com.dat.productorder.enums.ProductStatus;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.mapper.ProductMapper;
import com.dat.productorder.repository.ProductRepository;
import com.dat.productorder.service.PublicProductService;
import com.dat.productorder.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicProductServiceImpl implements PublicProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductSummaryResponse> getProducts(String keyword, Long categoryId, Pageable pageable) {

        Specification<Product> spec = Specification.<Product>unrestricted()
                .and(ProductSpecification.isActiveProduct())
                .and(ProductSpecification.isActiveCategory())
                .and(ProductSpecification.hasKeyword(keyword))
                .and(ProductSpecification.hasCategory(categoryId));

        Page<Product> products = productRepository.findAll(spec, pageable);

        return products.map(productMapper::toSummaryResponse);
    }

    @Override
    public ProductDetailResponse getProductById(Long id) {

        Product product = productRepository.findByIdAndStatusAndCategoryStatus(
                id, ProductStatus.ACTIVE, CategoryStatus.ACTIVE).orElseThrow(
                        () -> new ResourceNotFoundException("Product not found"));

        return productMapper.toDetailResponse(product);
    }
}
