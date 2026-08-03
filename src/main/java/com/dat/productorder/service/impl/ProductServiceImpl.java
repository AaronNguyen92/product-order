package com.dat.productorder.service.impl;

import com.dat.productorder.dto.request.product.CreateProductRequest;
import com.dat.productorder.dto.response.product.AdminProductResponse;
import com.dat.productorder.dto.request.product.UpdateProductRequest;
import com.dat.productorder.entity.Category;
import com.dat.productorder.entity.Product;
import com.dat.productorder.enums.CategoryStatus;
import com.dat.productorder.enums.ProductStatus;
import com.dat.productorder.exception.BadRequestException;
import com.dat.productorder.exception.ConflictException;
import com.dat.productorder.exception.DuplicateResourceException;
import com.dat.productorder.exception.ResourceNotFoundException;
import com.dat.productorder.mapper.ProductMapper;
import com.dat.productorder.repository.CategoryRepository;
import com.dat.productorder.repository.ProductRepository;
import com.dat.productorder.service.FileStorageService;
import com.dat.productorder.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png");

    @Override
    public AdminProductResponse createProduct(CreateProductRequest request) {

        String normalizedName = request.getName().trim();

        if (productRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateResourceException(
                    "Product name already exists"
            );
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        if (category.getStatus() == CategoryStatus.INACTIVE) {
            throw new ConflictException(
                    "Category is inactive"
            );
        }

        Product product = productMapper.toEntity(request);

        product.setName(normalizedName);
        product.setCategory(category);
        product.setStatus(ProductStatus.ACTIVE);

        Product savedProduct = productRepository.save(product);

        log.info("Admin created product '{}'.", product.getName());

        return productMapper.toResponse(savedProduct);

    }

    @Override
    public AdminProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found") );

        return productMapper.toResponse(product);

    }

    @Override
    public List<AdminProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();

        List<AdminProductResponse> responses = new ArrayList<>();

        for (Product product : products) {
            responses.add(productMapper.toResponse(product));
        }

        return responses;

    }

    @Override
    public AdminProductResponse updateProduct(Long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found"));

        request.setName(request.getName().trim());

        if (productRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new DuplicateResourceException("Product name already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getStatus() == CategoryStatus.INACTIVE) {
            throw new ConflictException("Category is inactive");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        productRepository.save(product);

        log.info("Admin updated product '{}'.", product.getName());

        return productMapper.toResponse(product);

    }

    @Override
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);

        log.info("Admin deleted product '{}'.", product.getName());

    }

    @Override
    public AdminProductResponse uploadImage(Long productId, MultipartFile file) {

        //Find Product
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found"));

        //Check image null or empty
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is required");
        }

        //Check img size
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BadRequestException("Image size must not exceed 10 MB");
        }

        //Check img extension
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new BadRequestException("Invalid image file");
        }

        int lastDot = originalFilename.lastIndexOf('.');

        if (lastDot == -1) {
            throw new BadRequestException("Image must have a valid extension");
        }

        String extension = originalFilename.substring(lastDot + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Only JPG, JPEG and PNG images are allowed");
        }

        //Validate MIME type
        String contentType = file.getContentType();

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new BadRequestException("Only JPG, JPEG and PNG images are allowed");
        }

        //Create img filename
        String fileName = "product-" + productId + "." + extension;

        String imageUrl = fileStorageService.store(file, "products", fileName);

        product.setImageUrl(imageUrl);

        productRepository.save(product);

        log.info("Admin uploaded image for product '{}'.", product.getName());

        return productMapper.toResponse(product);

    }

}
