package com.pos.webPos.payment;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductRepository;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.callback.CallbackHandler;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hibernate.query.sqm.tree.SqmNode.log;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PaymentServiceTest {
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PaymentService paymentService;
    @Autowired private PosSessionRepository posSessionRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;

    private PosSession paymentTestSession;
    private Product product1;
    private Product product2;

    @BeforeEach
    public void beforeEach() {
        paymentTestSession = new PosSession("paymentTestSession", LocalDateTime.now());
        this.posSessionRepository.save(paymentTestSession);

        Category defaultCategory = new Category("기본", paymentTestSession);
        this.categoryRepository.save(defaultCategory);

        product1 = new Product(defaultCategory,"product1",10000,paymentTestSession);
        this.productRepository.save(product1);

        product2 = new Product(defaultCategory, "product2", 20000, paymentTestSession);
        this.productRepository.save(product2);
    }

    @DisplayName("결제 저장 성공")
    @Test
    void save_success() {
        // given
        Payment payment = new Payment("현금", 10000, paymentTestSession);

        // when
        this.paymentService.save(payment);

        // then
        assertThat(payment.getPaymentMethod()).isEqualTo("현금");
        assertThat(payment.getTotalPrice()).isEqualTo(10000);
        assertThat(payment.getPosSession()).isEqualTo(paymentTestSession);
    }

    @DisplayName("결제 삭제 성공")
    @Test
    void deletePayment_success() {
        // given
        Payment deletePayment = new Payment("현금", 10000, paymentTestSession);
        this.paymentRepository.save(deletePayment);

        // when
        this.paymentService.deletePayment(deletePayment);

        // then
        assertThrows(DataNotFoundException.class, () -> {
            this.paymentService.getPayment(deletePayment.getId());
        });
    }

    @DisplayName("결제 삭제 실패")
    @Test
    void deletePayment_fail() {
        // given
        Payment invalidPayment = new Payment("현금", 10000, paymentTestSession);

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.paymentService.deletePayment(invalidPayment);
        });
    }

    @DisplayName("결제 조회 성공")
    @Test
    void getPayment_success() {
        // given
        Payment testPayment = new Payment("현금", 10000, paymentTestSession);
        this.paymentRepository.save(testPayment);

        // when
        Payment getPayment = this.paymentService.getPayment(testPayment.getId());

        // then
        assertThat(getPayment.getId()).isEqualTo(testPayment.getId());
        assertThat(getPayment.getPaymentMethod()).isEqualTo(testPayment.getPaymentMethod());
        assertThat(getPayment.getPosSession()).isEqualTo(testPayment.getPosSession());
        assertThat(getPayment.getTotalPrice()).isEqualTo(testPayment.getTotalPrice());
    }

    @DisplayName("결제 조회 실패")
    @Test
    void getPayment_fail() {
        // given
        Payment invalidPayment = new Payment("현금", 10000, paymentTestSession);

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.paymentService.getPayment(invalidPayment.getId());
        });
    }

    @DisplayName("세션으로 결제 리스트 조회 성공")
    @Test
    void getPaymentList_success() {
        // given
        Payment payment1 = new Payment("현금", 10000, paymentTestSession);
        this.paymentRepository.save(payment1);

        Payment payment2 = new Payment("카드", 20000, paymentTestSession);
        this.paymentRepository.save(payment2);

        // when
        List<Payment> paymentList = this.paymentService.getPaymentList(paymentTestSession);

        // then
        assertThat(paymentList).isNotNull();
        assertThat(paymentList).hasSize(2);
        assertThat(paymentList.get(0).getPosSession()).isEqualTo(paymentTestSession);
        assertThat(paymentList.get(1).getPosSession()).isEqualTo(paymentTestSession);
    }

    @DisplayName("세션으로 결제 리스트 조회 실패")
    @Test
    void getPaymentList_fail() {
        // given
        PosSession invalidSession = new PosSession("invalidSession", LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.paymentService.getPaymentList(invalidSession);
        });
    }

    @DisplayName("세션으로 현금결제 총 금액 조회 성공")
    @Test
    void getTotalCashPayments_success() {
        // given
        Payment cashPayment1 = new Payment("현금", 10000, paymentTestSession);
        this.paymentRepository.save(cashPayment1);

        Payment cashPayment2 = new Payment("현금", 20000, paymentTestSession);
        this.paymentRepository.save(cashPayment2);

        Payment cardPayment = new Payment("카드", 15000, paymentTestSession);
        this.paymentRepository.save(cardPayment);

        // when
        Integer totalCashAmount = this.paymentService.getTotalCashPayments(paymentTestSession);

        // then
        assertThat(totalCashAmount).isEqualTo(cashPayment1.getTotalPrice()+cashPayment2.getTotalPrice());
    }

    @DisplayName("세션으로 현금결제 총 금액 조회 실패")
    @Test
    void getTotalCashPayments_fail() {
        // given
        PosSession invalidSession = new PosSession("invalidSession", LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.paymentService.getTotalCashPayments(invalidSession);
        });
    }

    @DisplayName("세션으로 카드결제 총 금액 조회 성공")
    @Test
    void getTotalCardPayments_success() {
        // given
        Payment cardPayment1 = new Payment("카드", 50000, paymentTestSession);
        this.paymentRepository.save(cardPayment1);

        Payment cardPayment2 = new Payment("카드", 30000, paymentTestSession);
        this.paymentRepository.save(cardPayment2);

        Payment cashPayment = new Payment("현금", 50000, paymentTestSession);
        this.paymentRepository.save(cashPayment);

        // when
        Integer totalCardAmount = this.paymentService.getTotalCardPayments(paymentTestSession);

        // then
        assertThat(totalCardAmount).isEqualTo(cardPayment1.getTotalPrice()+cardPayment2.getTotalPrice());
    }

    @DisplayName("세션으로 카드결제 총 금액 조회 실패")
    @Test
    void getTotalCardPayments_fail() {
        // given
        PosSession invalidSession = new PosSession("invalidSession", LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.paymentService.getTotalCardPayments(invalidSession);
        });
    }
}