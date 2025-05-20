package com.pos.webPos.controller;

import com.pos.webPos.session.PosSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final PosSessionService posSessionService;

    @GetMapping("/")
    public String home(HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();

        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        System.out.println(sessionId+" "+ LocalDateTime.now());
        return "home";
    }
}
