package org.niewie.personapi.security;

import io.jsonwebtoken.Claims;
import org.niewie.personapi.util.TokenHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author aniewielska
 * @since 19/07/2018
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenHandler tokenHandler;

    protected JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, TokenHandler tokenHandler) {
        super(requiresAuthenticationRequestMatcher);
        this.tokenHandler = tokenHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null) {
            throw new AuthenticationException("Ups") {

            };
        }
        try {
            authHeader = authHeader.replaceFirst("^Bearer ", "");
            Claims claims = tokenHandler.verifyToken(authHeader);
            return new UsernamePasswordAuthenticationToken(claims.getSubject(),
                    "", AuthorityUtils.commaSeparatedStringToAuthorityList("USER"));
        } catch (Exception e) {
            throw new AuthenticationException("Ups") {

            };

        }
    }

}
