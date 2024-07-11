package com.project.demo.logic.entity.product;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.project.demo.logic.entity.product.ProductDTO(p.id, p.name,p.description,p.price,p.stock, c.id, c.name) " +
            "FROM Product p JOIN p.category c")
    List<ProductDTO> findAllProductsWithCategory();
}
