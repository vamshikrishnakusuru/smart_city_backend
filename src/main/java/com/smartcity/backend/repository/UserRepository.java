package com.smartcity.backend.repository;

import com.smartcity.backend.entity.User;
import com.smartcity.backend.enums.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRoleAndCityIgnoreCase(Role role, String city);
}
