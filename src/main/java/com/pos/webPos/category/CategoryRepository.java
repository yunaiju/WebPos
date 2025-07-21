package com.pos.webPos.category;

import com.pos.webPos.product.Product;
import com.pos.webPos.session.PosSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Boolean existsByPosSessionAndCategoryName(PosSession posSession, String categoryName);

    List<Category> findAllByPosSession(PosSession posSession);

    Category findByCategoryNameAndPosSession(String categoryName, PosSession posSession);

    Category findByCategoryName(String categoryName);

    @Modifying
    @Query("DELETE FROM Category c WHERE c.posSession.id = :posSessionId")
    void deleteByPosSessionId(@Param("posSessionId") Integer posSessionId);
}
