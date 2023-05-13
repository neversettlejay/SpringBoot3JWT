package com.jaytech.security.storage;

import com.jaytech.security.models.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {

    @Query(value = "SELECT * FROM roles WHERE role IN ?1", nativeQuery = true)
    Optional<Set<Roles>> findByRoleNames(List<String> roleNames);

    @Query(value = "SELECT * FROM roles WHERE role IN ?1 LIMIT 1", nativeQuery = true)
    Optional<Roles> findFirstByRoleNames(List<String> roleNames);


    /*
//    Not working

    @Query(value = "SELECT DISTINCT role "
            + "FROM (SELECT UNNEST(?1::text[]) AS role) AS roles "
            + "WHERE NOT EXISTS ( "
            + "  SELECT 1 "
            + "  FROM roles "
            + "  WHERE role ILIKE roles.role "
            + ")",
            nativeQuery = true)
    List<String> findMissingRoles(List<String> roles);*/
}
