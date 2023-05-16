package com.jaytech.security.configurations.filters;

import com.jaytech.security.models.payload.transfer.EntityCreatedEvent;
import com.jaytech.security.service.implementation.AccessTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter implements
        ApplicationListener<EntityCreatedEvent> {

    private HttpServletRequest incomingRequest;


    private final AccessTokenService accessTokenService;
    // There are many implementations of UserDetailsService available but we want our own implementation
    private final UserDetailsService userDetailsService;

    @Override
    public void onApplicationEvent(EntityCreatedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof HttpServletRequest) {
            incomingRequest = (HttpServletRequest) event.getEntity();
        }
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
//The first thing we do is check whether the request has JWT token
        final String authorizationHeader = request.getHeader("Authorization");//authenticationHeader is a part of our request
        final String jwtToken;
        final String username;
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
//           String identityUserNameInJwtTokenPresent= Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode("6A586E3272357538782F413F4428472B4B6250655368566B5970337336763979244226452948404D635166546A576E5A7134743777217A25432A462D4A614E645267556B58703273357538782F413F4428472B4B6250655368566D597133743677397A244226452948404D635166546A576E5A7234753778214125442A462D4A614E645267556B58703273357638792F423F4528482B4B6250655368566D597133743677397A24432646294A404E635166546A576E5A7234753778214125442A472D4B6150645367556B58703273357638792F423F4528482B4D6251655468576D597133743677397A24432646294A404E635266556A586E327234753778214125442A472D4B6150645367566B59703373367638792F423F4528482B4D6251655468576D5A7134743777217A24432646294A404E635266556A586E3272357538782F413F442A472D4B6150645367566B59703373367639792442264529482B4D6251655468576D5A7134743777217A25432A462D4A614E635266556A586E3272357538782F413F4428472B4B6250655367566B5970337336763979244226452948404D635166546A576D5A7134743777217A25432A462D4A614E645267556B58703272357538782F413F4428472B4B6250655368566D5971337436763979244226452948404D635166546A576E5A7234753778217A25432A462D4A614E645267"))).build().parseClaimsJws(authorizationHeader.substring(7)).getBody().getSubject();
            log.info("Authorization header not found in request '" + incomingRequest.getRequestURI() + "'");
//            log.info("As header is not present '"+identityUserNameInJwtTokenPresent+"' the request will be unauthorized");

            filterChain.doFilter(request, response);//if authorizationHeader is null or is in improper format, send request and the response to the next filter
            return;
        }
        jwtToken = authorizationHeader.substring(7);
        // After checking JWT token we need to call UserDetailsService to check whether user already exist in our database, but to do that we need to extract the username
        username = accessTokenService.extractUsernameFromJwtToken(jwtToken);
        if (Objects.nonNull(username) && /*check whether user is not already authenticated, so that we will not have to update SecurityContextHolder again*/ Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            // as we have got the username and we know user is not yet authenticated , we need to get user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (accessTokenService.checkIfJwtTokenIsValid(jwtToken, userDetails)) {
// If user is valid then we need to update our securityContext and sent the request to dispatcher servlet
                //UsernamePasswordAuthenticationToken object is needed by spring in order to update our security context
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //build details out of our http request
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                System.out.println(usernamePasswordAuthenticationToken);
            }
        }
        // pass the request to the next filters to be executed
        filterChain.doFilter(request, response);
    }
}
