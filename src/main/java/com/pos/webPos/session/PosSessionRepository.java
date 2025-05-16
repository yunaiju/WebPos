package com.pos.webPos.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PosSessionRepository extends JpaRepository<PosSession, Integer> {
    Optional<PosSession> findBySessionId(String sessionId);
}
