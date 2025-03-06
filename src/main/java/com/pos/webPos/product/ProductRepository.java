package com.pos.webPos.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByCategoryId(Integer categoryId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p")
    void deleteAllProducts();
}
