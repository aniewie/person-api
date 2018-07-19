package org.niewie.personapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author aniewielska
 * @since 19/07/2018
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String certificate;
    private String keystorePath;
    private String keystorePassword;
    private String keyAlias;
    private String keyPassword;

}
