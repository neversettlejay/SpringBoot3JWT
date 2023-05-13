package com.jaytech.security.controller.roles;

import com.jaytech.security.models.entities.Features;
import com.jaytech.security.models.entities.Roles;
import com.jaytech.security.models.payload.roles.Feature;
import com.jaytech.security.models.payload.roles.Operation;
import com.jaytech.security.service.implementation.RolesService;
import com.jaytech.security.storage.RolesRepository;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import com.jaytech.security.models.payload.roles.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/back-end/user/role")
@RequiredArgsConstructor
public class RolesController {

    private final RolesService rolesService;
    private final RolesRepository rolesRepository;

    @PostMapping
    ResponseEntity<CustomHttpResponse> addNewRole(@RequestBody Role role) {
        CustomHttpResponse customHttpResponse = rolesService.addNewRole(role);

        return switch (customHttpResponse.getHttpStatus()) {
            case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).body(customHttpResponse);
            case OK -> ResponseEntity.status(HttpStatus.OK).body(customHttpResponse);
            default -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(customHttpResponse);
        };
    }

    @PostMapping("/operation")
    ResponseEntity<CustomHttpResponse> addNewOperation(@RequestBody Operation operation) {
        CustomHttpResponse customHttpResponse = rolesService.addNewOperation(operation);

        return switch (customHttpResponse.getHttpStatus()) {
            case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).body(customHttpResponse);
            case OK -> ResponseEntity.status(HttpStatus.OK).body(customHttpResponse);
            default -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(customHttpResponse);
        };
    }

    @PostMapping("/feature")
    ResponseEntity<CustomHttpResponse> addNewFeature(@RequestBody Feature feature) {
        CustomHttpResponse customHttpResponse = rolesService.addNewFeature(feature);

        return switch (customHttpResponse.getHttpStatus()) {
            case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).body(customHttpResponse);
            case OK -> ResponseEntity.status(HttpStatus.OK).body(customHttpResponse);
            default -> ResponseEntity.status(HttpStatus.NO_CONTENT).body(customHttpResponse);
        };
    }

    @GetMapping
    ResponseEntity<CustomHttpResponse> fetchAllRoles() {
        return ResponseEntity.ok(CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Fetched all the Roles successfully").data(rolesRepository.findAll().stream().map(roles -> new Role(roles.getRole(), roles.getDescription())).toList()).build());
    }
}
