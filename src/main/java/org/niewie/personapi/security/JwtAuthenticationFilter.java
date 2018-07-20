package org.niewie.personapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.niewie.personapi.exception.JwtExpiredTokenException;
import org.niewie.personapi.exception.JwtInvalidTokenException;
import org.niewie.personapi.exception.JwtNoTokenException;
import org.niewie.personapi.util.TokenHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter handling authentication by JWT token passed in the header
 * Because of Swagger limitations accepts token both with and withour Bearer prefix
 *
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
        if (authHeader == null || authHeader.startsWith("Basic")) {
            throw new JwtNoTokenException();
        }
        try {
            authHeader = authHeader.replaceFirst("^Bearer ", "");
            Claims claims = tokenHandler.verifyToken(authHeader);
            List<Object> roles = claims.get("roles", List.class);
            List<GrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(claims.getSubject(),
                    "", authorities);
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredTokenException();
        } catch (Exception e) {
            throw new JwtInvalidTokenException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }

}
