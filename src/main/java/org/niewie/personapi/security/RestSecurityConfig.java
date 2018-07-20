package org.niewie.personapi.security;

import org.niewie.personapi.util.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Config for authentication with JWT Token (everything except for /token)
 *
 * @author aniewielska
 * @since 19/07/2018
 */
@Configuration
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/favicon.ico"),
            new AntPathRequestMatcher("/error"),
            new AntPathRequestMatcher("/"),
            new AntPathRequestMatcher("/csrf"),
            //swagger docs
            new AntPathRequestMatcher("/v2/api-docs"),
            new AntPathRequestMatcher("/configuration/ui"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/**"),
            new AntPathRequestMatcher("/swagger-ui.html**"),
            new AntPathRequestMatcher("/webjars/**"));

    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);



    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    private TokenHandler tokenHandler;

    private JwtAuthenticationFilter authenticationFilter() {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(PROTECTED_URLS, tokenHandler);
        filter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler(resolver));
        return filter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                authorizeRequests().
                requestMatchers(PUBLIC_URLS).permitAll().
                anyRequest().authenticated();
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());
    }


}