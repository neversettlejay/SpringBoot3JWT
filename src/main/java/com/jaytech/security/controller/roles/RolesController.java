package com.jaytech.security.controller.roles;

import com.jaytech.security.models.entities.Roles;
import com.jaytech.security.storage.RolesRepository;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import com.jaytech.security.models.payload.roles.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/back-end/user/role")
@RequiredArgsConstructor
public class RolesController {

    private final RolesRepository rolesRepository;

    @PostMapping
    ResponseEntity<CustomHttpResponse> addNewRole(@RequestBody Role role) {
        Set<Roles> existingRoles = new HashSet<>();
        rolesRepository.findByRoleNames(List.of(role.getRole())).ifPresent(roles -> existingRoles.addAll(roles));

        if (!existingRoles.isEmpty())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CustomHttpResponse.builder().httpStatus(HttpStatus.CONFLICT).httpStatusCode(HttpStatus.CONFLICT.value()).message("Roles '" + existingRoles + "' already exist in database").build());


        var roleToAdd = Roles.builder()
                .role(role.getRole())
                .description(role.getDescription())
                .createdBy("Developer")
                .createdAt(LocalDateTime.now())
                .build();


        rolesRepository.save(roleToAdd);
        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Role '" + role.getRole() + "' added successfully").build());
    }

    @GetMapping
    ResponseEntity<CustomHttpResponse> fetchAllRoles() {
        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Fetched all the Roles successfully").data(rolesRepository.findAll().stream().map(roles -> new Role(roles.getRole(), roles.getDescription())).toList()).build());
    }
}
