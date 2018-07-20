package org.niewie.personapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtInvalidTokenException extends JwtException {
    public JwtInvalidTokenException() {
        super("Invalid token");
    }
}
