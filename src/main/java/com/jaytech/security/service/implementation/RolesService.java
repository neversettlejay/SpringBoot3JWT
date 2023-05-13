package com.jaytech.security.service.implementation;

import com.jaytech.security.models.entities.Features;
import com.jaytech.security.models.entities.Operations;
import com.jaytech.security.models.entities.Roles;
import com.jaytech.security.models.payload.roles.Feature;
import com.jaytech.security.models.payload.roles.Operation;
import com.jaytech.security.models.payload.roles.Role;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import com.jaytech.security.storage.FeaturesRepository;
import com.jaytech.security.storage.OperationsRepository;
import com.jaytech.security.storage.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;
    private final OperationsRepository operationsRepository;
    private final FeaturesRepository featuresRepository;

    public CustomHttpResponse addNewRole(Role role) {


        Set<Roles> existingRoles = new HashSet<>();

        // find if existing roles with same names exist
        rolesRepository.findByRoleNames(List.of(role.getRole())).ifPresent(existingRoles::addAll);

        // if set not empty it means existing role names match the role we want to add
        if (!existingRoles.isEmpty())
            return CustomHttpResponse.builder().httpStatus(HttpStatus.CONFLICT).httpStatusCode(HttpStatus.CONFLICT.value()).message("Roles '" + existingRoles + "' already exist in database").build();

        // check if features exist
        if (featuresRepository.findByFeatureNames(List.of(role.getFeature())).isEmpty())
            return CustomHttpResponse.builder().httpStatus(HttpStatus.CONFLICT).httpStatusCode(HttpStatus.CONFLICT.value()).message("Features '" + role.getFeature() + "' doesnt exist in database").build();
        // check if operations exist
        List<String> operationsNotPresentInDb = new ArrayList<>();
        role.getOperations().forEach(operation -> {
            if (operationsRepository.findByOperationName(List.of(operation)).isEmpty()) // operation not present in the database
                operationsNotPresentInDb.add(operation);
        });
        if (operationsNotPresentInDb.size() > 0)
            return CustomHttpResponse.builder().httpStatus(HttpStatus.CONFLICT).httpStatusCode(HttpStatus.CONFLICT.value()).message("Operations '" + operationsNotPresentInDb + "' doesn't exist in database").build();

        //validations completed

        Features featureToAdd = featuresRepository.findFirstByFeatureNames(List.of(role.getFeature())).get();

        Collection<Operations> operationsToAdd = role.getOperations().stream().map(operation -> operationsRepository.findFirstByOperationName(List.of(operation)).get()).collect(Collectors.toList());
        featureToAdd.setOperations(operationsToAdd);

        // to create a relation between feature and operations
        featuresRepository.save(featureToAdd);

        var roleToAdd = Roles.builder()
                .role(role.getRole())
                .description(role.getDescription())
                .features(List.of(featureToAdd))
                .createdBy(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication()) ? SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous")
                .createdAt(LocalDateTime.now())
                .build();

        //to create relation between role and feature
        rolesRepository.save(roleToAdd);
        return CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Role '" + role.getRole() + "' added successfully").build();
    }

    public CustomHttpResponse addNewOperation(Operation operation) {
        Set<Operations> existingOperations = new HashSet<>();
        operationsRepository.findByOperationName(List.of(operation.getOperation())).ifPresent(existingOperations::addAll);

        if (!existingOperations.isEmpty())
            return CustomHttpResponse.builder().httpStatus(HttpStatus.CONFLICT).httpStatusCode(HttpStatus.CONFLICT.value()).message("Operations '" + existingOperations + "' already exist in database").build();


        var operationsToAdd = Operations.builder()
                .operation(operation.getOperation())
                .description(operation.getDescription())
                .createdBy(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication()) ? SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous")
                .createdAt(LocalDateTime.now())
                .build();


        operationsRepository.save(operationsToAdd);
        return CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Operation '" + operation.getOperation() + "' added successfully").build();


    }

    public CustomHttpResponse addNewFeature(Feature feature) {
        Set<Features> existingFeatures = new HashSet<>();
        featuresRepository.findByFeatureNames(List.of(feature.getFeature())).ifPresent(existingFeatures::addAll);

        if (!existingFeatures.isEmpty())
            return CustomHttpResponse.builder().httpStatus(HttpStatus.CONFLICT).httpStatusCode(HttpStatus.CONFLICT.value()).message("Features '" + existingFeatures + "' already exist in database").build();


        var featureToAdd = Features.builder()
                .feature(feature.getFeature())
                .description(feature.getDescription())
                .createdBy(Objects.nonNull(SecurityContextHolder.getContext().getAuthentication()) ? SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous")
                .createdAt(LocalDateTime.now())
                .build();


        featuresRepository.save(featureToAdd);
        return CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Role '" + feature.getFeature() + "' added successfully").build();

    }

}
