package org.niewie.personapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

/**
 * Generation and verification/parsing of JWT token
 * Keys are asymmetric pair - but stored together (could be well a symmetric key in this case)
 * Demo Keystore in classpath; credentials in application.properties
 *
 * @author aniewielska
 * @since 19/07/2018
 */
@Component
public class TokenHandler {

    private final static int DURATION_SEC = 300;

    private final KeyProvider keyProvider;


    public TokenHandler(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public String generateToken(String userName, Collection<String> roles) {
        Instant issueTime = Instant.now();
        Instant expiryTime = issueTime.plusSeconds(DURATION_SEC);

        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", roles)
                .setIssuedAt(Date.from(issueTime))
                .setExpiration(Date.from(expiryTime))
                .signWith(SignatureAlgorithm.RS256, keyProvider.getPrivateKey())
                .compact();
    }

    public Claims verifyToken(String token) {
        return Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody();
    }

}
