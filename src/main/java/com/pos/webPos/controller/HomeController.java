package com.pos.webPos.controller;

import com.pos.webPos.session.PosSessionService;
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
    public String home(HttpSession session) {
        String sessionId = session.getId();
        this.posSessionService.getOrCreatePosSession(sessionId);
        System.out.println(sessionId+" "+ LocalDateTime.now());
        return "home";
    }
}
