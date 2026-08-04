package com.dat.productorder.controller;

import com.dat.productorder.dto.request.product.CreateProductRequest;
import com.dat.productorder.dto.request.product.UpdateProductRequest;
import com.dat.productorder.dto.response.product.AdminProductResponse;
import com.dat.productorder.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Tag(
        name = "Product Management",
        description = "APIs for managing Products"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @Operation(summary = "Admin - Create Product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminProductResponse createProduct(@Valid @RequestBody CreateProductRequest request) {

        return productService.createProduct(request);
    }


    @Operation(summary = "Admin - Get Product Detail")
    @GetMapping("/{id}")
    public AdminProductResponse getProductById(@PathVariable Long id) {

        return productService.getProductById(id);

    }


    @Operation(summary = "Admin - Get Product List")
    @GetMapping
    public List<AdminProductResponse> getAllProducts() {

        return productService.getAllProducts();

    }


    @Operation(summary = "Admin - Update Product")
    @PutMapping("/{id}")
    public AdminProductResponse updateProduct(@PathVariable Long id,
                                              @Valid @RequestBody UpdateProductRequest request) {

        return productService.updateProduct(id, request);

    }


    @Operation(summary = "Admin - Delete Product")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

    }


    @Operation(summary = "Admin - Upload Product Image")
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdminProductResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        return productService.uploadImage(id, file);

    }

}
