package com.jaytech.security.service.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService implements com.jaytech.security.service.definition.JwtService {

    //created from: In a JWT (JSON Web Token), claims are pieces of information about the authenticated user or the token itself. Claims are represented as key-value pairs and can include things like the user ID, username, expiration time, and other custom attributes.
    private static final String SECRET_KEY = "6A586E3272357538782F413F4428472B4B6250655368566B5970337336763979244226452948404D635166546A576E5A7134743777217A25432A462D4A614E645267556B58703273357538782F413F4428472B4B6250655368566D597133743677397A244226452948404D635166546A576E5A7234753778214125442A462D4A614E645267556B58703273357638792F423F4528482B4B6250655368566D597133743677397A24432646294A404E635166546A576E5A7234753778214125442A472D4B6150645367556B58703273357638792F423F4528482B4D6251655468576D597133743677397A24432646294A404E635266556A586E327234753778214125442A472D4B6150645367566B59703373367638792F423F4528482B4D6251655468576D5A7134743777217A24432646294A404E635266556A586E3272357538782F413F442A472D4B6150645367566B59703373367639792442264529482B4D6251655468576D5A7134743777217A25432A462D4A614E635266556A586E3272357538782F413F4428472B4B6250655367566B5970337336763979244226452948404D635166546A576D5A7134743777217A25432A462D4A614E645267556B58703272357538782F413F4428472B4B6250655368566D5971337436763979244226452948404D635166546A576E5A7234753778217A25432A462D4A614E645267";

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
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(Map<String, Object> listOfClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(listOfClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())/*when was the token created*/)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)/*how long a token should be valid*/)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
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
