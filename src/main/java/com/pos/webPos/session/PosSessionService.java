package com.pos.webPos.session;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.payment.PaymentRepository;
import com.pos.webPos.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

    public PosSession getOrCreatePosSession(String sessionId) {
        return this.posSessionRepository.findBySessionId(sessionId)
                .orElseGet(()-> {
                    PosSession newSession = new PosSession(sessionId, LocalDate.now());
                    this.posSessionRepository.save(newSession);

                    Category defaultCategory = new Category("기본", newSession);
                    categoryRepository.save(defaultCategory);

                    return newSession;
                });
    }

    @Transactional
    public void deletePosSessionWithChildren(PosSession posSession) {
        this.productRepository.deleteByPosSessionId(posSession.getId());
        this.categoryRepository.deleteByPosSessionId(posSession.getId());
        this.paymentRepository.deleteByPosSessionId(posSession.getId());
        this.posSessionRepository.delete(posSession);
    }
}
