package com.pos.webPos.payment;

import com.pos.webPos.session.PosSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByPosSession(PosSession posSession);

    @Query("SELECT SUM(p.totalPrice) FROM Payment p WHERE p.posSession.id = :posSessionId AND p.paymentMethod = '현금'")
    Integer findTotalPriceByPosSessionIdForCashPayments(@Param("posSessionId") Integer posSessionId);

    @Query("SELECT SUM(p.totalPrice) FROM Payment p WHERE p.posSession.id = :posSessionId AND p.paymentMethod = '카드'")
    Integer findTotalPriceByPosSessionIdForCardPayments(@Param("posSessionId") Integer posSessionId);

    @Modifying
    @Query("DELETE FROM Payment p WHERE p.posSession.id = :posSessionId")
    void deleteByPosSessionId(@Param("posSessionId") Integer posSessionId);
}
