package com.jaytech.security.service.implementation;

import com.jaytech.security.configurations.PropertyConfigurations;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AccessTokenService implements com.jaytech.security.service.definition.JwtService {

    private final PropertyConfigurations propertyConfigurations;
    //created from: In a JWT (JSON Web Token), claims are pieces of information about the authenticated user or the token itself. Claims are represented as key-value pairs and can include things like the user ID, username, expiration time, and other custom attributes.
//    private static final String SECRET_KEY = "anything_you_like";

    @Override
    public String extractUsernameFromJwtToken(String jwtToken) {
        return extractClaim(jwtToken, claims -> claims.getSubject());
    }

    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {

        /*
         * In a JWT (JSON Web Token), claims are pieces of information about the authenticated user
         *  or the token itself. Claims are represented as key-value pairs and can include things like
         *  the user ID, username, expiration time, and other custom attributes.
         * */

        /*
         * What is a Sign in key?
         *
         * In the context of Json web tokens a sign in key is used to digitally sign JWT.
         * The sign in key is used to create the signature part of the JWT which is used to verify
         *  that the sender of the JWT is who it claims to be and ensure that the message wasn't changed along the way.
         * */
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(propertyConfigurations.getSecretKey())))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    public String generateJwtToken(Map<String, Object> listOfClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(listOfClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())/*when was the token created*/)
                .setIssuer(StringUtils.isNotBlank(SecurityContextHolder.getContext().getAuthentication().getName()) ? SecurityContextHolder.getContext().getAuthentication().getName() : "anonymous")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)/*how long a token should be valid*/)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(propertyConfigurations.getSecretKey())), SignatureAlgorithm.HS256)
                .compact();
    }

    /*I want to generate token without extractClaims, just using userDetails itself*/
    public String generateJwtTokenWithoutExtractingClaims(UserDetails userDetails) {
        return generateJwtToken(new HashMap<>(), userDetails);
    }

    public Boolean checkIfJwtTokenIsValid(String jwtToken, UserDetails userDetails) {

        final String usernameExtractedFromJwtToken = extractUsernameFromJwtToken(jwtToken);
        return (usernameExtractedFromJwtToken.equals(userDetails.getUsername())) && !isJwtTokenExpired(jwtToken);


    }

    private Boolean isJwtTokenExpired(String jwtToken) {
        // if expiry would be of future it will return false
        return extractExpiryTimeOfJwtToken(jwtToken).before(new Date());
    }

    private Date extractExpiryTimeOfJwtToken(String jwtToken) {
        return extractClaim(jwtToken, claims -> claims.getExpiration());
    }

    private String extractSubjectFromClaimsUsingJwtToken(String jwtToken) {
        return extractClaim(jwtToken, claims -> claims.getSubject());
    }


    private String extractIssuerFromClaimsUsingJwtToken(String jwtToken) {
        return extractClaim(jwtToken, claims -> claims.getIssuer());
    }

}
