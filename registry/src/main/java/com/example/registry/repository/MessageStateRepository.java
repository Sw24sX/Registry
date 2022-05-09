package com.example.registry.repository;

import com.example.registry.model.MessageState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageStateRepository extends JpaRepository<MessageState, String> {
}