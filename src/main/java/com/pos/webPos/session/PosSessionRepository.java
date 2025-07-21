package com.pos.webPos.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PosSessionRepository extends JpaRepository<PosSession, Integer> {
    Optional<PosSession> findBySessionId(String sessionId);
}
