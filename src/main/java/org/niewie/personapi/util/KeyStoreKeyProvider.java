package org.niewie.personapi.util;

import org.niewie.personapi.config.JwtProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * @author aniewielska
 * @since 20/07/2018
 */
@Component
public class KeyStoreKeyProvider implements KeyProvider {

    private final JwtProperties jwtProperties;
    private Key privateKey;
    private PublicKey publicKey;

    public KeyStoreKeyProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    public void initKeys() throws Exception {
        ClassPathResource resource = new ClassPathResource(jwtProperties.getKeystorePath());
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(resource.getInputStream(), jwtProperties.getKeystorePassword().toCharArray());

        privateKey = keystore.getKey(jwtProperties.getKeyAlias(), jwtProperties.getKeyPassword().toCharArray());
        Certificate cert = keystore.getCertificate(jwtProperties.getKeyAlias());
        publicKey = cert.getPublicKey();
    }

    @Override
    public Key getPrivateKey() {
        return privateKey;
    }

    @Override
    public Key getPublicKey() {
        return publicKey;
    }
}
