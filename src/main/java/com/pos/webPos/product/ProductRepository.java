package com.pos.webPos.product;

import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Boolean existsByPosSessionAndProductName(PosSession posSession, String productName);

    List<Product> findAllByPosSession(PosSession posSession);

    List<Product> findByCategoryIdAndPosSession(Integer categoryId, PosSession posSession);

    Product findByProductName(String productName);

    @Modifying
    @Query("DELETE FROM Product p WHERE p.posSession.id = :posSessionId")
    void deleteByPosSessionId(@Param("posSessionId") Integer posSessionId);
}
