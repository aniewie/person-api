package org.niewie.personapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
public class TokenHandlerTest {

    public TokenHandlerTest() throws NoSuchAlgorithmException {
    }

    private static class GeneratedKeyProvider implements KeyProvider {
        private Key privateKey;
        private Key publicKey;

        GeneratedKeyProvider() throws NoSuchAlgorithmException {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        }

        @Override
        public Key getPrivateKey() {
            return privateKey;
        }

        @Override
        public Key getPublicKey() {
            return publicKey;
        }
    }


    private KeyProvider keyProvider = new GeneratedKeyProvider();

    private TokenHandler tokenHandler = new TokenHandler(keyProvider);

    @Test
    public void generateToken() {
        String token = tokenHandler.generateToken("abc", new ArrayList<>());
        Claims claims = Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody();
        assertThat(claims.getSubject(), is("abc"));
        List<Object> roles = claims.get("roles", List.class);
        assertThat(roles, is(is(empty())));
        Instant from = claims.getIssuedAt().toInstant();
        Instant to = claims.getExpiration().toInstant();
        int seconds = Long.valueOf(Duration.between(from, to).getSeconds()).intValue();
        assertThat(seconds, is(600));
    }

    @Test
    public void generateToken_roles() {
        String token = tokenHandler.generateToken("abc", Arrays.asList("xx", "yy"));
        Claims claims = Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody();
        List<String> roles = (List<String>) claims.get("roles", List.class);
        assertThat(roles, containsInAnyOrder("xx", "yy"));

    }

    @Test
    public void generateToken_null_roles() {
        String token = tokenHandler.generateToken("abc", null);
        Claims claims = Jwts.parser().setSigningKey(keyProvider.getPublicKey()).parseClaimsJws(token).getBody();
        List<String> roles = (List<String>) claims.get("roles", List.class);
        assertThat(roles, is(nullValue()));

    }

    @Test
    public void verifyToken() {
        Date from = new Date();
        Date to = Date.from(from.toInstant().plusSeconds(10));
        String token = Jwts.builder()
                .setSubject("abc")
                .claim("roles", Arrays.asList("xx"))
                .setIssuedAt(from)
                .setExpiration(to)
                .signWith(SignatureAlgorithm.RS256, keyProvider.getPrivateKey())
                .compact();
        Claims claims = tokenHandler.verifyToken(token);
        assertThat(claims.getSubject(), is("abc"));
        int seconds = Long.valueOf(Duration.between(claims.getExpiration().toInstant(), claims.getIssuedAt().toInstant()).getSeconds()).intValue();
        assertThat(seconds, is(-10));
        List<String> roles = (List<String>) claims.get("roles", List.class);
        assertThat(roles, contains("xx"));
    }

    @Test(expected = ExpiredJwtException.class)
    public void verifyToken_expired() {
        Date from = Date.from(Instant.now().minusSeconds(20));
        Date to = Date.from(from.toInstant().plusSeconds(10));
        String token = Jwts.builder()
                .setSubject("abc")
                .claim("roles", Arrays.asList("xx"))
                .setIssuedAt(from)
                .setExpiration(to)
                .signWith(SignatureAlgorithm.RS256, keyProvider.getPrivateKey())
                .compact();
        tokenHandler.verifyToken(token);
    }

}