package com.pos.webPos.controller;

import com.pos.webPos.category.CategoryService;
import com.pos.webPos.payment.Payment;
import com.pos.webPos.payment.PaymentService;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductService;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/close")
public class CloseController {

    private final PaymentService paymentService;
    private final PosSessionService posSessionService;

    @GetMapping("/payments")
    public String payments(Model model, HttpSession session) {
        String sessionId = session.getId();
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Payment> payments = this.paymentService.getPaymentList(posSession);
        model.addAttribute("payments", payments);

        model.addAttribute("today", LocalDate.now());

        return "close/payments";
    }

    @GetMapping("/payments/{id}/delete")
    public String deletePayment(@PathVariable("id")Long id) {
        Payment payment = this.paymentService.getPayment(id);
        this.paymentService.deletePayment(payment);

        return "redirect:/close/payments";
    }

    @GetMapping("/finish")
    public String close(Model model, HttpSession session) {
        String sessionId = session.getId();
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        Integer cashTotal = this.paymentService.getTotalCashPayments(posSession);
        if(cashTotal==null) cashTotal=0;
        Integer cardTotal = this.paymentService.getTotalCardPayments(posSession);
        if(cardTotal==null) cardTotal=0;
        Integer totalAmount = cashTotal+cardTotal;

        model.addAttribute("cashTotal",cashTotal);
        log.debug("cashTotal="+cashTotal);
        model.addAttribute("cardTotal",cardTotal);
        model.addAttribute("totalAmount",totalAmount);

        return "close/close";
    }

    @GetMapping("/reset")
    public String reset(HttpSession session) {
        String sessionId = session.getId();
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);
        this.posSessionService.deletePosSessionWithChildren(posSession);

        return "redirect:/";
    }
}
