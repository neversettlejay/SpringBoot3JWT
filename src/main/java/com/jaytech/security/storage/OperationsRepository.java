package com.jaytech.security.storage;

import com.jaytech.security.models.entities.Operations;
import com.jaytech.security.models.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OperationsRepository extends JpaRepository<Operations,Long> {

    @Query(value = "SELECT * FROM operations WHERE operation IN ?1", nativeQuery = true)
    Optional<Set<Operations>> findByOperationName(List<String> operationNames);

    @Query(value = "SELECT * FROM operations WHERE operation IN ?1 LIMIT 1", nativeQuery = true)
    Optional<Operations> findFirstByOperationName(List<String> operationNames);

}
