package com.pos.webPos.controller;

import com.pos.webPos.category.CategoryService;
import com.pos.webPos.payment.Payment;
import com.pos.webPos.payment.PaymentService;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final PaymentService paymentService;

    @GetMapping("/categories/{id}/products")
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable("id")Integer id) {
        List<Product> products = this.productService.getProductListByCategoryId(id);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id")Integer id) {
        Product product = this.productService.getProduct(id);

        return ResponseEntity.ok(product);
    }

    @PostMapping("/api/payCash")
    public ResponseEntity<Map<String, String>> payCash(@RequestBody Map<String, Object> data) {
        Integer totalPrice = Integer.parseInt(data.get("totalPrice").toString());

        // DB 저장
        Payment payment = new Payment("현금",totalPrice);
        paymentService.save(payment);

        // 성공 메시지 응답
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment saved");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/payCard")
    public ResponseEntity<Map<String, String>> payCard(@RequestBody Map<String, Object> data) {
        Integer totalPrice = Integer.parseInt(data.get("totalPrice").toString());

        // DB 저장
        Payment payment = new Payment("카드",totalPrice);
        paymentService.save(payment);

        // 성공 메시지 응답
        Map<String, String> response = new HashMap<>();
        response.put("message", "Payment saved");
        return ResponseEntity.ok(response);
    }
}
