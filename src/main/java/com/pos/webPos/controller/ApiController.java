package com.pos.webPos.controller;

import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductService;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final ProductService productService;
    private final PosSessionService posSessionService;

    @GetMapping("/categories/{categoryId}/products")
    public List<Product> getProductsByCategory(@PathVariable Integer categoryId, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        return productService.getProductsByCategoryAndSession(categoryId, posSession);
    }

    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable Integer productId, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        System.out.println(productService.getProduct(productId).getProductName());
        return productService.getProduct(productId);
    }
}
