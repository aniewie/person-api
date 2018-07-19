package org.niewie.personapi.util;

import io.jsonwebtoken.*;
import jdk.nashorn.internal.parser.Token;
import org.niewie.personapi.config.JwtProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aniewielska
 * @since 19/07/2018
 */
@Component
public class TokenHandler {
    private final JwtProperties jwtProperties;
    private Key privateKey;
    private PublicKey publicKey;

    public TokenHandler(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void initKeys() throws Exception {
        ClassPathResource resource = new ClassPathResource(jwtProperties.getKeystorePath());
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(resource.getInputStream(), jwtProperties.getKeystorePassword().toCharArray());

        privateKey = keystore.getKey(jwtProperties.getKeyAlias(), jwtProperties.getKeyPassword().toCharArray());
        Certificate cert = keystore.getCertificate(jwtProperties.getKeyAlias());
        publicKey = cert.getPublicKey();
    }
    public String generateToken(String userName, Collection<String> roles) {
        Instant issueTime = Instant.now();
        Instant expiryTime = issueTime.plusSeconds(180);

        String compactJws = Jwts.builder()
                .setSubject(userName)
                .claim("roles", roles)
                .setIssuedAt(Date.from(issueTime))
                .setExpiration(Date.from(expiryTime))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
        return compactJws;
    }

    public Claims verifyToken(String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

}
