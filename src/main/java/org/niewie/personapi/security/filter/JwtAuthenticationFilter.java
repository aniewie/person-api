package org.niewie.personapi.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.niewie.personapi.exception.JwtExpiredTokenException;
import org.niewie.personapi.exception.JwtInvalidTokenException;
import org.niewie.personapi.exception.JwtNoTokenException;
import org.niewie.personapi.security.jwt.TokenHandler;
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

import static org.niewie.personapi.security.jwt.TokenHandler.ROLES_CLAIM;


/**
 * Filter handling authentication by JWT token passed in the header
 * Because of Swagger limitations accepts token both with and without Bearer prefix
 *
 * @author aniewielska
 * @since 19/07/2018
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String AUTH_HEADER_KEY = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String BEARER_PREFIX_REGEX = "^Bearer ";
    public static final String BASIC_HEADER_PREFIX = "Basic";
    private final TokenHandler tokenHandler;

    public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher, TokenHandler tokenHandler) {
        super(requiresAuthenticationRequestMatcher);
        this.tokenHandler = tokenHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException {
        String authHeader = httpServletRequest.getHeader(AUTH_HEADER_KEY);
        if (authHeader == null || authHeader.startsWith(BASIC_HEADER_PREFIX)) {
            throw new JwtNoTokenException();
        }
        try {
            authHeader = authHeader.replaceFirst(BEARER_PREFIX_REGEX, "");
            Claims claims = tokenHandler.verifyToken(authHeader);
            List<Object> roles = claims.get(ROLES_CLAIM, List.class);
            List<GrantedAuthority> authorities = roles == null ? null : roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(claims.getSubject(),
                    "", authorities);
        } catch (ExpiredJwtException e) {
            //logger.debug("Expired token {}", e.getMessage());
            throw new JwtExpiredTokenException();
        } catch (Exception e) {
            logger.info("Invalid token");
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
