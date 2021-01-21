package org.gluu.jansadminuiapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gluu.jansadminuiapi.domain.types.config.OAuth2;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@Slf4j
public class SpringConfiguration {

    @Autowired
    ApplicationProperties applicationProperties;

    @Bean
    public SpringPluginManager pluginManager() {
        return new SpringPluginManager();
    }

    @Bean
    public AppConfiguration appConfiguration() {

        return new AppConfiguration(new OAuth2(
                applicationProperties.getAuthServer().getAuthzBaseUrl(),
                applicationProperties.getAuthServer().getClientId(),
                applicationProperties.getAuthServer().getClientSecret(),
                applicationProperties.getAuthServer().getScope(),
                applicationProperties.getAuthServer().getRedirectUrl(),
                applicationProperties.getAuthServer().getLogoutUrl(),
                applicationProperties.getAuthServer().getTokenEndpoint(),
                applicationProperties.getAuthServer().getIntrospectionEndpoint(),
                applicationProperties.getAuthServer().getUserInfoEndpoint()
        ));
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
