package com.project.demo.rest.product;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Product createProduct(@RequestBody Map<String, Object> productData) {
        Product product = new Product();
        product.setName((String) productData.get("name"));
        product.setDescription((String) productData.get("description"));
        product.setPrice((Integer) productData.get("price"));
        product.setStock((Integer) productData.get("stock"));

        Long categoryId = Long.valueOf((Integer) productData.get("categoryId"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> productData) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName((String) productData.get("name"));
                    existingProduct.setDescription((String) productData.get("description"));
                    existingProduct.setPrice((Integer) productData.get("price"));
                    existingProduct.setStock((Integer) productData.get("stock"));

                    Long categoryId = Long.valueOf((Integer) productData.get("categoryId"));
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found"));
                    existingProduct.setCategory(category);

                    return productRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    Product product = new Product();
                    product.setId(id);
                    product.setName((String) productData.get("name"));
                    product.setDescription((String) productData.get("description"));
                    product.setPrice((Integer) productData.get("price"));
                    product.setStock((Integer) productData.get("stock"));

                    Long categoryId = Long.valueOf((Integer) productData.get("categoryId"));
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found"));
                    product.setCategory(category);

                    return productRepository.save(product);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}