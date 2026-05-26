package com.medisecond.repository;

import com.medisecond.model.ChatMessage;
import com.medisecond.model.MedicalCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByMedicalCaseOrderByCreatedAtAsc(MedicalCase medicalCase);
}
