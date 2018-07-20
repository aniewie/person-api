package org.niewie.personapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that will be thrown, if any error occurred during JWT token parse
 * (other than Token expiration)
 *
 * @author aniewielska
 * @since 20/07/2018
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtInvalidTokenException extends JwtException {
    public JwtInvalidTokenException() {
        super("Invalid token");
    }
}
