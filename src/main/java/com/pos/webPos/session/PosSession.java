package com.pos.webPos.session;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity @Getter
public class PosSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sessionId;

    private LocalDateTime createdAt;

    public PosSession(String sessionId, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.createdAt = createdAt;
    }
}
