package org.niewie.personapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class JwtExpiredTokenException extends JwtException {

    public JwtExpiredTokenException() {
        super("Token expired");
    }
}
