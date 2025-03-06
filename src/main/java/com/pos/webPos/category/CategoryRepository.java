package com.pos.webPos.category;

import com.pos.webPos.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String categoryName);

    @Transactional
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id <> 1")
    void deleteAllCategories();
}
