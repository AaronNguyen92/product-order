package com.dat.productorder.specification;

import com.dat.productorder.entity.Product;
import com.dat.productorder.enums.CategoryStatus;
import com.dat.productorder.enums.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasKeyword(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + keyword.toLowerCase() + "%"
                );

    }

    public static Specification<Product> hasCategory(Long categoryId){

        if (categoryId == null) {
            return null;
        }

        return (root, query, cb) ->
                cb.equal(
                root.get("category").get("id"),
                categoryId
        );

    }

    public static Specification<Product> isActiveProduct() {

        return (root, query, cb) ->
                cb.equal(root.get("status"), ProductStatus.ACTIVE);

    }

    public static Specification<Product> isActiveCategory() {

        return (root, query, cb) ->
                cb.equal(
                        root.get("category").get("status"),
                        CategoryStatus.ACTIVE
                );

    }

}
