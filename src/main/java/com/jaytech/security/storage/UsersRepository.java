package com.jaytech.security.storage;

import com.jaytech.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users,Long> {
Optional<Users> findByUsername(String username);
}
