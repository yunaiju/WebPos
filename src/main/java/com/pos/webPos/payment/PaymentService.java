package com.pos.webPos.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public void save(Payment payment) {
        this.paymentRepository.save(payment);
    }

    public Integer getTotalCashPayments() {
        // '현금' 결제의 totalPrice 합을 구함
        return this.paymentRepository.findTotalPriceForCashPayments();
    }

    public Integer getTotalCardPayments() {
        // '현금' 결제의 totalPrice 합을 구함
        return this.paymentRepository.findTotalPriceForCardPayments();
    }

    public void resetPayment() {
        this.paymentRepository.deleteAllPayments();
    }
}
