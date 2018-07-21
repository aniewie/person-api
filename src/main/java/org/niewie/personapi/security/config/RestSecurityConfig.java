package org.niewie.personapi.security.config;

import org.niewie.personapi.security.filter.JwtAuthenticationFailureHandler;
import org.niewie.personapi.security.filter.JwtAuthenticationFilter;
import org.niewie.personapi.security.filter.JwtAuthenticationSuccessHandler;
import org.niewie.personapi.security.jwt.TokenHandler;
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

    //If that looks familiar, tribute to GDM - just the list is mine
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
        //The list of protected URLs has to be passed to Filter - permitAll for public
        //does not prevent public files from being included in filterChain
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
