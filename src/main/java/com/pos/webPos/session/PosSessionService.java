package com.pos.webPos.session;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.payment.PaymentRepository;
import com.pos.webPos.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PosSessionService {
    private final PosSessionRepository posSessionRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    public PosSession getPosSessionOrElse(String sessionId) {
        return this.posSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new DataNotFoundException("PosSession Not Found"));
    }

    public boolean existOrCreatePosSession(String sessionId) {
        Optional<PosSession> posSession = this.posSessionRepository.findBySessionId(sessionId);

        if(posSession.isPresent()) {
            return false;
        }

        PosSession newSession = new PosSession(sessionId, LocalDateTime.now());
        this.posSessionRepository.save(newSession);

        Category defaultCategory = new Category("기본", newSession);
        categoryRepository.save(defaultCategory);

        return true;
    }

    @Transactional
    public void deletePosSessionWithChildren(PosSession posSession) {
        this.productRepository.deleteByPosSessionId(posSession.getId());
        System.out.println("product deleted");
        this.categoryRepository.deleteByPosSessionId(posSession.getId());
        System.out.println("category deleted");
        this.paymentRepository.deleteByPosSessionId(posSession.getId());
        System.out.println("payment deleted");
        this.posSessionRepository.deleteById(posSession.getId());
        System.out.println("session deleted");

        try {
            this.posSessionRepository.findBySessionId(posSession.getSessionId());
        } catch (Exception e) {
            throw new RuntimeException("No DELETED");
        }
    }
}
