package com.pos.webPos.payment;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.session.PosSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public void save(Payment payment) {
        this.paymentRepository.save(payment);
    }

    public void deletePayment(Payment payment) {this.paymentRepository.delete(payment);}

    public Payment getPayment(Long id) { return this.paymentRepository.findById(id)
            .orElseThrow(()-> new DataNotFoundException("payment not found")); }

    public List<Payment> getPaymentList(PosSession posSession) {
        return this.paymentRepository.findAllByPosSession(posSession);
    }

    public Integer getTotalCashPayments(PosSession posSession) {
        // '현금' 결제의 totalPrice 합을 구함
        try {
            // 데이터베이스 조회 로직
            return this.paymentRepository.findTotalPriceByPosSessionIdForCashPayments(posSession.getId());
        } catch (Exception e) {
            log.error("Error fetching total cash payments", e);
            return 0;  // 예외 발생 시 기본값 반환
        }
    }

    public Integer getTotalCardPayments(PosSession posSession) {
        // '카드' 결제의 totalPrice 합을 구함
        try {
            // 데이터베이스 조회 로직
            return this.paymentRepository.findTotalPriceByPosSessionIdForCardPayments(posSession.getId());
        } catch (Exception e) {
            log.error("Error fetching total cash payments", e);
            return 0;  // 예외 발생 시 기본값 반환
        }
    }
}
