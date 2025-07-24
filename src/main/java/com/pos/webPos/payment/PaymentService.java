package com.pos.webPos.payment;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PosSessionRepository posSessionRepository;

    public void save(Payment payment) {
        this.paymentRepository.save(payment);
    }

    public void deletePayment(Payment payment) {
        if(payment.getId()!=null) {
            Optional<Payment> validPayment = this.paymentRepository.findById(payment.getId());
            if(validPayment.isPresent()) {
                this.paymentRepository.delete(payment);
            } else {
                throw new DataNotFoundException("payment not found");
            }
        } else {
            throw new DataNotFoundException("payment not found");
        }
    }

    public Payment getPayment(Long id) {
        if (id != null) {
            Optional<Payment> validPayment = this.paymentRepository.findById(id);
            if (validPayment.isPresent()) {
                return validPayment.get();
            } else {
                throw new DataNotFoundException("payment not found");
            }
        } else {
            throw new DataNotFoundException("payment not found");
        }
    }

    public List<Payment> getPaymentList(PosSession posSession) {
        if(posSession.getId()!=null) {
            Optional<PosSession> validSession = this.posSessionRepository.findById(posSession.getId());
            if(validSession.isPresent()) {
                return this.paymentRepository.findAllByPosSession(posSession);
            } else {
                throw new DataNotFoundException("session not found");
            }
        } else {
            throw new DataNotFoundException("session not found");
        }
    }

    public Integer getTotalCashPayments(PosSession posSession) {
        if(posSession.getId()!=null) {
            Optional<PosSession> validSession = this.posSessionRepository.findById(posSession.getId());
            if(validSession.isPresent()) {
                return this.paymentRepository.findTotalPriceByPosSessionIdForCashPayments(posSession.getId());
            } else {
                throw new DataNotFoundException("session not found");
            }
        } else {
            throw new DataNotFoundException("session not found");
        }
    }

    public Integer getTotalCardPayments(PosSession posSession) {
        if(posSession.getId()!=null) {
            Optional<PosSession> validSession = this.posSessionRepository.findById(posSession.getId());
            if(validSession.isPresent()) {
                return this.paymentRepository.findTotalPriceByPosSessionIdForCardPayments(posSession.getId());
            } else {
                throw new DataNotFoundException("session not found");
            }
        } else {
            throw new DataNotFoundException("session not found");
        }
    }
}
