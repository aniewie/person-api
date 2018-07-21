package org.niewie.personapi.security.jwt;

import java.security.Key;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
public interface KeyProvider {
    Key getPrivateKey();

    Key getPublicKey();
}
