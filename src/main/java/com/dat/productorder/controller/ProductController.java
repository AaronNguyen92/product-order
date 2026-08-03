package com.dat.productorder.controller;

import com.dat.productorder.dto.request.product.CreateProductRequest;
import com.dat.productorder.dto.request.product.UpdateProductRequest;
import com.dat.productorder.dto.response.product.AdminProductResponse;
import com.dat.productorder.service.ProductService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {

        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public AdminProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<AdminProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public AdminProductResponse updateProduct(@PathVariable Long id,
                                              @Valid @RequestBody UpdateProductRequest request) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
    }

    @PostMapping("/{id}/image")
    public AdminProductResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        return productService.uploadImage(id, file);
    }

}
