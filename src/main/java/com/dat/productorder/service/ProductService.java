package com.dat.productorder.service;

import com.dat.productorder.dto.request.product.CreateProductRequest;
import com.dat.productorder.dto.request.product.UpdateProductRequest;
import com.dat.productorder.dto.response.product.AdminProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    AdminProductResponse createProduct(CreateProductRequest request);
    AdminProductResponse getProductById(Long id);
    List<AdminProductResponse> getAllProducts();
    AdminProductResponse updateProduct(Long id, UpdateProductRequest request);
    void deleteProduct(Long id);
    AdminProductResponse uploadImage(Long productId, MultipartFile file);
}
