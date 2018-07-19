package org.niewie.personapi.security;

import org.niewie.personapi.util.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

/**
 * @author aniewielska
 * @since 19/07/2018
 */
@Configuration
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenHandler tokenHandler;

    private JwtAuthenticationFilter authenticationFilter() {
        return new JwtAuthenticationFilter(AnyRequestMatcher.INSTANCE, tokenHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests().
                antMatchers(HttpMethod.GET, "/", "/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**").permitAll().
                anyRequest().authenticated().
                and().csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint());


    }
}
