package org.niewie.personapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.niewie.personapi.config.JwtProperties;
import org.niewie.personapi.util.TokenHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Controller for obtaining JWT token
 *
 * @author aniewielska
 * @since 19/07/2018
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenHandler tokenHandler;

    public TokenController(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @RequestMapping(method = RequestMethod.GET)
    String getToken(Principal principal, Authentication authentication) {

        return this.tokenHandler.generateToken(principal.getName(), authentication.getAuthorities().
                stream().map(GrantedAuthority::getAuthority).
                collect(Collectors.toList()));
    }
}
