package com.jaytech.security.configurations;

import com.jaytech.security.storage.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * In JWT-based authentication, the server typically validates the user's authenticity and authorization based on the information present in the JWT token. There are a few parameters within the JWT that the server can use to determine the validity of the user:
 *
 * Issuer (iss): The "iss" claim indicates the issuer or the entity that issued the JWT. The server can verify the issuer's identity and trustworthiness. It can check if the issuer is a trusted authority in the authentication process.
 *
 * Subject (sub): The "sub" claim identifies the subject of the JWT, which can represent the user or entity associated with the token. The server can use this information to identify and authenticate the user.
 *
 * Signature Verification: The server can verify the integrity and authenticity of the JWT by validating the signature. The signature is generated using a secret key or a public-private key pair. By verifying the signature, the server ensures that the token hasn't been tampered with and was indeed issued by a trusted entity.
 *
 * In addition to these parameters, the server may also consider other custom claims or additional information within the JWT to determine the validity and authorization of the user. The specific validation process can vary depending on the implementation and requirements of the server-side application.
 */

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfigurations {
    private final UsersRepository usersRepository;

    //Bean always should be public
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        /*
        * AuthenticationProvider is the data access object which is responsible to fetch the user details and also encode passwords.
        *
        * For this AuthenticationProvider we have many implementations but here we will implement DaoAuthenticationProvider
        * */

        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
