package com.example.registry.service.persistance.repository;

import com.example.registry.service.persistance.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, String> {

    boolean existsByLoginOrEmail(String login, String email);
}