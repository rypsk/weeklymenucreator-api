package com.rypsk.weeklymenucreator.api.repository;

import com.rypsk.weeklymenucreator.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationCode(String verificationCode);

    List<User> findAllByIsAutoEmailEnabled(boolean autoEmailEnabled);
}
