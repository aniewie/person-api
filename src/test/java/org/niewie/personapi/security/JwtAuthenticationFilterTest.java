package org.niewie.personapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.niewie.personapi.exception.JwtExpiredTokenException;
import org.niewie.personapi.exception.JwtInvalidTokenException;
import org.niewie.personapi.exception.JwtNoTokenException;
import org.niewie.personapi.util.TokenHandler;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
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
@RunWith(SpringRunner.class)
public class JwtAuthenticationFilterTest {

    @MockBean
    TokenHandler tokenHandler;

    private JwtAuthenticationFilter subject;

    private Claims claims;


    @Before
    public void setUp() {
        claims = new DefaultClaims();
        claims.setSubject("abc");
        claims.put("roles", Arrays.asList("xx", "yy"));

        this.subject = new JwtAuthenticationFilter(new AntPathRequestMatcher("/"), tokenHandler);
    }

    @Test
    public void attemptAuthentication_bearer() throws IOException, ServletException {
        given(tokenHandler.verifyToken("XYZ")).willReturn(claims);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer XYZ");
        Authentication authentication = subject.attemptAuthentication(request, new MockHttpServletResponse());
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.addAll(authentication.getAuthorities());
        List<String> roles = auths.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(authentication.getPrincipal().toString(), is("abc"));
        assertThat(roles, containsInAnyOrder("xx", "yy"));

    }

    @Test
    public void attemptAuthentication_noprefix() throws IOException, ServletException {
        given(tokenHandler.verifyToken("XYZ")).willReturn(claims);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "XYZ");
        Authentication authentication = subject.attemptAuthentication(request, new MockHttpServletResponse());
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.addAll(authentication.getAuthorities());
        List<String> roles = auths.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(authentication.getPrincipal().toString(), is("abc"));
        assertThat(roles, containsInAnyOrder("xx", "yy"));

    }

    @Test(expected = JwtInvalidTokenException.class)
    public void attemptAuthentication_token_exception() throws IOException, ServletException {
        given(tokenHandler.verifyToken("XYZ")).willThrow(RuntimeException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "XYZ");
        subject.attemptAuthentication(request, new MockHttpServletResponse());
    }


    @Test(expected = JwtNoTokenException.class)
    public void attemptAuthentication_no_token() throws IOException, ServletException {
        given(tokenHandler.verifyToken("XYZ")).willReturn(claims);
        MockHttpServletRequest request = new MockHttpServletRequest();
        subject.attemptAuthentication(request, new MockHttpServletResponse());
    }

    @Test(expected = JwtExpiredTokenException.class)
    public void attemptAuthentication_expired_token() throws IOException, ServletException {
        given(tokenHandler.verifyToken("XYZ")).willThrow(ExpiredJwtException.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "XYZ");
        subject.attemptAuthentication(request, new MockHttpServletResponse());
    }

}