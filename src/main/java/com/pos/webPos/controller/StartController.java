package com.pos.webPos.controller;

import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryService;
import com.pos.webPos.payment.Payment;
import com.pos.webPos.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/start")
public class StartController {

    private final CategoryService categoryService;
    private final PaymentService paymentService;

    @GetMapping("/main")
    public String main(Model model) {
        List<Category> categories = this.categoryService.getList();
        model.addAttribute("categories",categories);

        return "start/main";
    }

    @GetMapping("/payCash")
    public String payCash(@RequestParam("totalPrice") Integer totalPrice, Model model) {
        model.addAttribute("totalPrice", totalPrice);

        return "start/payCash";
    }

    @GetMapping("/payCard")
    public String payCard(@RequestParam("totalPrice") Integer totalPrice, Model model) {
        model.addAttribute("totalPrice", totalPrice);

        return "start/payCard"; // 결제 확인 페이지
    }

    @PostMapping("/save/cash")
    public String saveCash(@RequestParam("totalPrice") Integer totalPrice,
                           @RequestParam("receivedAmount")Integer receivedAmount,
                           Model model) {
        Integer change = receivedAmount - totalPrice;

        Payment payment = new Payment("현금",totalPrice);
        this.paymentService.save(payment);

        model.addAttribute("change",change);

        return "start/changePopup";
    }

    @PostMapping("/save/card")
    public String saveCard(@RequestParam("totalPrice")Integer totalPrice) {
    Payment payment = new Payment("카드",totalPrice);
    this.paymentService.save(payment);

    return "redirect:/start/main";
    }
}
