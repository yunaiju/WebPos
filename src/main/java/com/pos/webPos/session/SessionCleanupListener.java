package com.pos.webPos.session;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class SessionCleanupListener implements HttpSessionListener {
    private final PosSessionService posSessionService;

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        System.out.println("session timeout");

        HttpSession session = event.getSession();
        String sessionId = session.getId();
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        if (posSession!=null) {
            System.out.println(sessionId+" "+ LocalDateTime.now());
            this.posSessionService.deletePosSessionWithChildren(posSession);
            System.out.println("data deleted");
        }
    }
}
