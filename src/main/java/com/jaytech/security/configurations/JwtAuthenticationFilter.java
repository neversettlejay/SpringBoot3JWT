package com.jaytech.security.configurations;

import com.jaytech.security.service.implementation.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/*
* We want this filter to be active every time we get request
* We can either do it by implementing methods in Filter interface by implementing Filter interface
* Or as OncePerRequestFilter already implements Filter interface we will use it.
*
* */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // There are many implementations of UserDetailsService available but we want our own implementation
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//The first thing we do is check whether the request has JWT token
        final String authorizationHeader=request.getHeader("Authorization");//authenticationHeader is a part of our request
        final String jwtToken;
        final String username;
        if(Objects.isNull(authorizationHeader) || authorizationHeader.startsWith("Bearer ")){
        filterChain.doFilter(request,response);//if authorizationHeader is null or is in improper format, send request and the response to the next filter
       return;
        }
        jwtToken=authorizationHeader.substring(7);
        // After checking JWT token we need to call UserDetailsService to check whether user already exist in our database, but to do that we need to extract the username
        username=jwtService.extractUsernameFromJwtToken(jwtToken);
        if(Objects.nonNull(username) && /*check whether user is not already authenticated, so that we will not have to update SecurityContextHolder again*/ Objects.isNull(SecurityContextHolder.getContext().getAuthentication())){
            // as we have got the username and we know user is not yet authenticated , we need to get user details from the database
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(username);
            if(jwtService.checkIfJwtTokenIsValid(jwtToken,userDetails)){
// If user is valid then we need to update our securityContext and sent the request to dispatcher servlet
               //UsernamePasswordAuthenticationToken object is needed by spring in order to update our security context
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //build details out of our http request
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        // pass the request to the next filters to be executed
        filterChain.doFilter(request,response);
    }
}
