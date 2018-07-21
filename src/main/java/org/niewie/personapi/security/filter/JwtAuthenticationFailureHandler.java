package org.niewie.personapi.security.filter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Delegates resolution of exception from JWT Filter to wired resolver (firing our CustomErrorHandler)
 *
 * @author aniewielska
 * @since 20/07/2018
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFailureHandler(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        resolver.resolveException(request, response, null, exception);
    }
}
