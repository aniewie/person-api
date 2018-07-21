package org.niewie.personapi.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.niewie.personapi.exception.JwtExpiredTokenException;
import org.niewie.personapi.exception.JwtInvalidTokenException;
import org.niewie.personapi.exception.JwtNoTokenException;
import org.niewie.personapi.security.jwt.TokenHandler;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author aniewielska
 * @since 20/07/2018
 */

public class JwtAuthenticationFilterTest {


    private TokenHandler tokenHandler;

    private JwtAuthenticationFilter subject;

    private Claims claims;


    @Before
    public void setUp() {
        claims = new DefaultClaims();
        claims.setSubject("abc");
        claims.put("roles", Arrays.asList("xx", "yy"));
        tokenHandler = Mockito.mock(TokenHandler.class);
        this.subject = new JwtAuthenticationFilter(new AntPathRequestMatcher("/"), tokenHandler);
    }

    @Test
    public void attemptAuthentication_bearer() {
        given(tokenHandler.verifyToken("XYZ")).willReturn(claims);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer XYZ");
        Authentication authentication = subject.attemptAuthentication(request, new MockHttpServletResponse());
        List<GrantedAuthority> auths = new ArrayList<>(authentication.getAuthorities());
        List<String> roles = auths.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(authentication.getPrincipal().toString(), is("abc"));
        assertThat(roles, containsInAnyOrder("xx", "yy"));

    }

    @Test
    public void attemptAuthentication_noprefix() {
        given(tokenHandler.verifyToken("XYZ")).willReturn(claims);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "XYZ");
        Authentication authentication = subject.attemptAuthentication(request, new MockHttpServletResponse());
        List<GrantedAuthority> auths = new ArrayList<>(authentication.getAuthorities());
        List<String> roles = auths.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(authentication.getPrincipal().toString(), is("abc"));
        assertThat(roles, containsInAnyOrder("xx", "yy"));

    }

    @Test(expected = JwtInvalidTokenException.class)
    public void attemptAuthentication_token_exception() {
        given(tokenHandler.verifyToken("XYZ")).willThrow(RuntimeException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "XYZ");
        subject.attemptAuthentication(request, new MockHttpServletResponse());
    }


    @Test(expected = JwtNoTokenException.class)
    public void attemptAuthentication_no_token() {
        given(tokenHandler.verifyToken("XYZ")).willReturn(claims);
        MockHttpServletRequest request = new MockHttpServletRequest();
        subject.attemptAuthentication(request, new MockHttpServletResponse());
    }

    @Test(expected = JwtExpiredTokenException.class)
    public void attemptAuthentication_expired_token() {
        given(tokenHandler.verifyToken("XYZ")).willThrow(ExpiredJwtException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "XYZ");
        subject.attemptAuthentication(request, new MockHttpServletResponse());
    }

}