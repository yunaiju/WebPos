package com.pos.webPos.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query("SELECT SUM(p.totalPrice) FROM Payment p WHERE p.paymentMethod = '현금'")
    Integer findTotalPriceForCashPayments();

    @Query("SELECT SUM(p.totalPrice) FROM Payment p WHERE p.paymentMethod = '카드'")
    Integer findTotalPriceForCardPayments();

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p")
    void deleteAllPayments();
}
