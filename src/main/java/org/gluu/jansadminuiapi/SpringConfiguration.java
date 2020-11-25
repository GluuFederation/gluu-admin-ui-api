package org.gluu.jansadminuiapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gluu.jansadminuiapi.domain.types.config.OAuth2;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@Slf4j
public class SpringConfiguration {

    @Bean
    public SpringPluginManager pluginManager() {
        return new SpringPluginManager();
    }

    @Bean
    public AppConfiguration appConfiguration() {
        // TODO Load configuration from the DB.
        String clientId = "1001.70aa88c0-cf99-4f81-b963-9c2ca59cdd5c";
        String clientSecret = "hJtZ4h36fTUe";
        String authzBaseUrl = "https://ce-ob.gluu.org/oxauth/authorize.htm";
        String scope = "openid+profile+email+user_name";
        String redirectUrl = "https://ce-ob.gluu.org/admin-ui";
        String logoutUrl = "https://ce-ob.gluu.org/admin-ui";
        String tokenEndpoint = "https://ce-ob.gluu.org/oxauth/restv1/token";
        String introspectionEndpoint = "https://ce-ob.gluu.org/oxauth/restv1/introspection";

        return new AppConfiguration(new OAuth2(authzBaseUrl, clientId, clientSecret, scope, redirectUrl, logoutUrl, tokenEndpoint, introspectionEndpoint));
    }

    @Bean
    public RestTemplate idPClient(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofMillis(15000))
            .setReadTimeout(Duration.ofMillis(30000))
            .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
