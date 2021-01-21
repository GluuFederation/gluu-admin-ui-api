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

    public static class AuthServer {
        String clientId;
        String clientSecret;
        String authzBaseUrl;
        String scope;
        String redirectUrl;
        String logoutUrl;
        String tokenEndpoint;
        String introspectionEndpoint;
        String userInfoEndpoint;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getAuthzBaseUrl() {
            return authzBaseUrl;
        }

        public void setAuthzBaseUrl(String authzBaseUrl) {
            this.authzBaseUrl = authzBaseUrl;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getRedirectUrl() {
            return redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

        public String getLogoutUrl() {
            return logoutUrl;
        }

        public void setLogoutUrl(String logoutUrl) {
            this.logoutUrl = logoutUrl;
        }

        public String getTokenEndpoint() {
            return tokenEndpoint;
        }

        public void setTokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
        }

        public String getIntrospectionEndpoint() {
            return introspectionEndpoint;
        }

        public void setIntrospectionEndpoint(String introspectionEndpoint) {
            this.introspectionEndpoint = introspectionEndpoint;
        }

        public String getUserInfoEndpoint() {
            return userInfoEndpoint;
        }

        public void setUserInfoEndpoint(String userInfoEndpoint) {
            this.userInfoEndpoint = userInfoEndpoint;
        }
    }
}
