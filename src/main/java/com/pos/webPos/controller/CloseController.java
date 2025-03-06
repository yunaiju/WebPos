package com.pos.webPos.controller;

import com.pos.webPos.category.CategoryService;
import com.pos.webPos.payment.PaymentService;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/close")
public class CloseController {

    private final PaymentService paymentService;
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("")
    public String close(Model model) {
        Integer cashTotal = this.paymentService.getTotalCashPayments();
        if(cashTotal==null) cashTotal=0;
        Integer cardTotal = this.paymentService.getTotalCardPayments();
        if(cardTotal==null) cardTotal=0;
        Integer totalAmount = cashTotal+cardTotal;

        model.addAttribute("cashTotal",cashTotal);
        model.addAttribute("cardTotal",cardTotal);
        model.addAttribute("totalAmount",totalAmount);

        return "close/close";
    }

    @GetMapping("/reset")
    public String reset() {
        this.productService.resetProduct();
        this.categoryService.resetCategory();
        this.paymentService.resetPayment();

        return "redirect:/";
    }
}
