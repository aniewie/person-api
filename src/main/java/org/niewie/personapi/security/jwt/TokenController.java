package org.niewie.personapi.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.stream.Collectors;

/**
 * Controller for obtaining JWT token
 *
 * @author aniewielska
 * @since 19/07/2018
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenHandler tokenHandler;

    public TokenController(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getToken(Principal principal, Authentication authentication) {

        return this.tokenHandler.generateToken(principal.getName(), authentication.getAuthorities().
                stream().map(GrantedAuthority::getAuthority).
                collect(Collectors.toList()));
    }
}
