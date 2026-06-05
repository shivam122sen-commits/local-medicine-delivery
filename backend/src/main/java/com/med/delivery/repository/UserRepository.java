package com.med.delivery.repository;

import com.med.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRememberMeToken(String token);
    List<User> findByRole(User.Role role);
    List<User> findByRoleAndIsOnline(User.Role role, Boolean isOnline);
}
