package org.gluu.jansadminuiapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties
@PropertySource("classpath:application.properties")
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProperties {
    private AuthServer authServer;
    private TokenServer tokenServer;

    @Data
    public static class AuthServer {
        String host;
        String username;
        String password;
        String clientId;
        String clientSecret;
        String authzBaseUrl;
        String scope;
        String redirectUrl;
        String logoutUrl;
        String tokenEndpoint;
        String introspectionEndpoint;
        String userInfoEndpoint;
    }

    @Data
    public static class TokenServer {
        String clientId;
        String clientSecret;
        String authzBaseUrl;
        String scope;
        String redirectUrl;
        String logoutUrl;
        String tokenEndpoint;
        String introspectionEndpoint;
        String userInfoEndpoint;
    }
}
