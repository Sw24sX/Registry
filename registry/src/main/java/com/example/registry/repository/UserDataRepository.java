package com.example.registry.repository;

import com.example.registry.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, String> {

    boolean existsByLoginOrEmail(String login, String email);
}