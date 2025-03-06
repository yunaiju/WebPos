package com.pos.webPos.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;

import java.security.Principal;
import java.time.LocalDateTime;

@Entity
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String paymentMethod;

    private Integer totalPrice;

    private LocalDateTime paymentTime=LocalDateTime.now();

    public Payment(String paymentMethod, Integer amount) {
        this.paymentMethod=paymentMethod;
        this.totalPrice=amount;
    }
}
