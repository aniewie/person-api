package org.niewie.personapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author aniewielska
 * @since 18/07/2018
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException {

    private static final String message = "Person with Id=%s not found.";
    public PersonNotFoundException(String personId) {
        super(String.format(message, personId));
    }
}
