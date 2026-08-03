package com.dat.productorder.repository;

import com.dat.productorder.entity.Product;
import com.dat.productorder.enums.CategoryStatus;
import com.dat.productorder.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    boolean existsByCategoryId(Long categoryId);

    Optional<Product> findByIdAndStatusAndCategoryStatus(
            Long id,
            ProductStatus productStatus,
            CategoryStatus categoryStatus);
}
