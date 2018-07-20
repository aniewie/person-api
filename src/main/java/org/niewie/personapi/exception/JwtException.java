package org.niewie.personapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Super class for exceptions connected to JWT authentication
 *
 * @author aniewielska
 * @since 20/07/2018
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtException extends AuthenticationException {

    public JwtException(String msg) {
        super(msg);
    }

}
