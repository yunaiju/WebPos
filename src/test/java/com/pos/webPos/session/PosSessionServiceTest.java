package com.pos.webPos.session;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.category.CategoryService;
import com.pos.webPos.payment.Payment;
import com.pos.webPos.payment.PaymentRepository;
import com.pos.webPos.payment.PaymentService;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductRepository;
import com.pos.webPos.product.ProductService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PosSessionServiceTest {
    @Autowired private PosSessionRepository posSessionRepository;
    @Autowired private PosSessionService posSessionService;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ProductService productService;
    @Autowired private CategoryService categoryService;
    @Autowired private PaymentService paymentService;

    @DisplayName("세션 존재 확인 성공 - 세션 존재")
    @Test
    void getPosSessionOrElse_success() {
        // given
        PosSession session = new PosSession("testSessionId", LocalDateTime.now());
        this.posSessionRepository.save(session);
        String sessionId = session.getSessionId();

        // when
        PosSession getSession = this.posSessionService.getPosSessionOrElse(sessionId);

        // then
        assertThat(getSession.getId()).isEqualTo(session.getId());
        assertThat(getSession.getSessionId()).isEqualTo(session.getSessionId());
    }

    @DisplayName("세션 존재 확인 실패 - 세션 미존재")
    @Test
    void getPosSessionOrElse_fail_when_sessionNotFound() {
        // given
        String notSavedSessionId = "notSavedSessionId";

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.posSessionService.getPosSessionOrElse(notSavedSessionId);
        });
    }

    @DisplayName("세션 존재 확인 후 생성 결정 - 세션 존재")
    @Test
    void existOrCreatePosSession_exist() {
        // given
        PosSession session = new PosSession("sessionId", LocalDateTime.now());
        this.posSessionRepository.save(session);
        String sessionId = session.getSessionId();

        // when
        boolean result = this.posSessionService.existOrCreatePosSession(sessionId);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("세션 존재 확인 후 생성 결정 - 세션 미존재 -> 세션 생성")
    @Test
    void existOrCreatePosSession_create() {
        // given
        String fakeSessionId = "fakeSessionId";

        // when
        boolean result = this.posSessionService.existOrCreatePosSession(fakeSessionId);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("세션 관련 DB 모두 삭제 성공")
    @Test
    @Commit
    void deletePosSessionWithChildren_success() {
        // given
        PosSession session = new PosSession("testSessionId",LocalDateTime.now());
        this.posSessionRepository.saveAndFlush(session);
        String posSessionId = session.getSessionId();

        System.out.println(this.posSessionRepository.findBySessionId(posSessionId)+" DB에 저장 완료");

        Category category = new Category("testCategory",session);
        this.categoryRepository.saveAndFlush(category);
        Integer categoryId = category.getId();

        System.out.println(this.categoryRepository.findById(categoryId)+" DB에 저장 완료");

        Product product = new Product(category,"testProduct",5000,session);
        this.productRepository.saveAndFlush(product);
        Integer productId = product.getId();

        System.out.println(this.productRepository.findById(productId)+" DB에 저장 완료");

        Payment payment = new Payment("카드",5000,session);
        this.paymentRepository.saveAndFlush(payment);
        Long paymentId = payment.getId();

        System.out.println(this.paymentRepository.findById(paymentId)+" DB에 저장 완료");

        // when
        this.posSessionService.deletePosSessionWithChildren(session);

        // then
        assertThrows(DataNotFoundException.class, ()-> {
            this.posSessionService.getPosSessionOrElse(posSessionId);
        });
        assertThrows(DataNotFoundException.class, ()-> {
            this.productService.getProduct(productId);
        });
        assertThrows(DataNotFoundException.class, ()-> {
            this.categoryService.getCategory(categoryId);
        });
        assertThrows(DataNotFoundException.class, ()-> {
            this.paymentService.getPayment(paymentId);
        });
    }

    @DisplayName("세션 관련 DB 모두 삭제 실패")
    @Test
    void deletePosSessionWithChildren_fail() {
        // given
        PosSession deleteSession = new PosSession("sessionId",LocalDateTime.now());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            this.posSessionService.deletePosSessionWithChildren(deleteSession);
        });
    }
}