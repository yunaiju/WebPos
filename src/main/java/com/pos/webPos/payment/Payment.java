package com.pos.webPos.payment;

import com.pos.webPos.session.PosSession;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentMethod;

    private Integer totalPrice;

    private LocalDateTime paymentTime=LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private PosSession posSession;

    public Payment(String paymentMethod, Integer amount, PosSession posSession) {
        this.paymentMethod=paymentMethod;
        this.totalPrice=amount;
        this.posSession = posSession;
    }
}
