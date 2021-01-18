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
        String clientId = "1801.a0beec01-617b-4607-8a35-3e46ac43deb5";
        String clientSecret = "bGnCdaFdn11b";
        String authzBaseUrl = "https://gasmyr.gluu.org/jans-auth/authorize.htm";
        String scope = "openid+profile+email+user_name";
        String redirectUrl = "http://localhost:4100/";
        String logoutUrl = "http://localhost:4100/logout.htm";
        String tokenEndpoint = "https://gasmyr.gluu.org/jans-auth/restv1/token";
        String introspectionEndpoint = "https://gasmyr.gluu.org/jans-auth/restv1/introspection";

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
