package com.jaytech.security.storage;

import com.jaytech.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
Optional<Users> findByUsername(String username);
}
