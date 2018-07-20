package org.niewie.personapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.niewie.personapi.config.JwtProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
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
    private final KeyProvider keyProvider;

    public TokenHandler(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public String generateToken(String userName, Collection<String> roles) {
        Instant issueTime = Instant.now();
        Instant expiryTime = issueTime.plusSeconds(300);

        String compactJws = Jwts.builder()
                .setSubject(userName)
                .claim("roles", roles)
                .setIssuedAt(Date.from(issueTime))
                .setExpiration(Date.from(expiryTime))
                .signWith(SignatureAlgorithm.RS256, keyProvider.getPrivateKey())
                .compact();
        return compactJws;
    }

    public Claims verifyToken(String token) {
        return Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody();
    }

}
