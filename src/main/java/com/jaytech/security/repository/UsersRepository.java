package com.jaytech.security.repository;

import com.jaytech.security.models.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
Optional<Users> findByUsername(String username);
}
