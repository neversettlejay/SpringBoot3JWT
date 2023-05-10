package com.jaytech.security.service.implementation;

import com.jaytech.security.models.payload.users.AuthenticationRequest;
import com.jaytech.security.models.payload.users.AuthenticationResponse;
import com.jaytech.security.models.payload.users.RegisterRequest;
import com.jaytech.security.models.entities.Roles;
import com.jaytech.security.models.entities.Users;
import com.jaytech.security.storage.RolesRepository;
import com.jaytech.security.storage.UsersRepository;
import com.jaytech.security.models.payload.transfer.CustomHttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements com.jaytech.security.service.definition.AuthenticationService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public CustomHttpResponse registerUser(RegisterRequest registerRequest) {

        List<String> allRoles = rolesRepository.findAll().stream().map(Roles::getRole).toList();

        List<String> missingRolesFromTheDb = registerRequest.getRoles().stream().filter(roles -> !allRoles.contains(roles)).toList();

        if (!missingRolesFromTheDb.isEmpty()) {
            return CustomHttpResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).httpStatusCode(HttpStatus.BAD_REQUEST.value()).message("Roles '" + missingRolesFromTheDb + "' missing from the database").build();
        }

        List<String> availableRolesList = registerRequest.getRoles().stream().filter(allRoles::contains).toList();

        Set<Roles> availableRolesFromTheDb = rolesRepository.findByRoleNames(availableRolesList).orElseThrow(() -> new RuntimeException("Roles missing from the database"));

        var registerUser = Users.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .country(registerRequest.getCountry())
                .state(registerRequest.getState())
                .birthDate(registerRequest.getBirthDate())
                .phoneNumber(registerRequest.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .createdBy("JayTech")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .roles(availableRolesFromTheDb)
                .build();

        usersRepository.save(registerUser);

        availableRolesFromTheDb.forEach(roles -> roles.setUsersForRoles(registerUser));

        rolesRepository.saveAll(availableRolesFromTheDb);

        var jwtToken = jwtService.generateJwtTokenWithoutExtractingClaims(registerUser);
        return CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Successfully retrieved token").data(AuthenticationResponse.builder().jsonWebToken(jwtToken).build()).build();


    }

    @Override
    public  CustomHttpResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        //if we get to the below code that means the user is authenticated
        var user = usersRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username '" + authenticationRequest.getUsername() + "' not found"));
        var jwtToken = jwtService.generateJwtTokenWithoutExtractingClaims(user);
        return CustomHttpResponse.builder().httpStatus(HttpStatus.OK).httpStatusCode(HttpStatus.OK.value()).message("Successfully retrieved token").data(AuthenticationResponse.builder().jsonWebToken(jwtToken).build()).build();
    }
}
