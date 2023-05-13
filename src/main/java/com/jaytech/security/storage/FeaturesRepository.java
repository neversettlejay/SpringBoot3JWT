package com.jaytech.security.storage;

import com.jaytech.security.models.entities.Features;
import com.jaytech.security.models.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FeaturesRepository extends JpaRepository<Features,Long> {

    @Query(value = "SELECT * FROM features WHERE feature IN ?1", nativeQuery = true)
    Optional<Set<Features>> findByFeatureNames(List<String> featureNames);

    @Query(value = "SELECT * FROM features WHERE feature IN ?1 LIMIT 1", nativeQuery = true)
    Optional<Features> findFirstByFeatureNames(List<String> featureNames);

}
