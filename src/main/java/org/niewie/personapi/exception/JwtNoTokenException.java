package org.niewie.personapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception representing no token/wrong type (like Basic) token in Authorisation header,
 * Not very spec compliant - 'No token' should result in no body response,
 * but we will signal this with this exception (rendering response body)
 *
 * @author aniewielska
 * @since 20/07/2018
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtNoTokenException extends JwtException {
    public JwtNoTokenException() {
        super("No token");
    }
}
