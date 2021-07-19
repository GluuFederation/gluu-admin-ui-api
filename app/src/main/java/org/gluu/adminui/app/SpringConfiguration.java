package org.gluu.adminui.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.app.domain.types.config.LicenseConfiguration;
import org.gluu.adminui.app.domain.types.config.OAuth2;
import org.gluu.adminui.app.services.AppConfiguration;
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
    public AppConfiguration appConfiguration() {

        return new AppConfiguration(new OAuth2(
                applicationProperties.getAuthServer().getAuthzBaseUrl(),
                applicationProperties.getAuthServer().getClientId(),
                applicationProperties.getAuthServer().getClientSecret(),
                applicationProperties.getAuthServer().getScope(),
                applicationProperties.getAuthServer().getRedirectUrl(),
                applicationProperties.getAuthServer().getFrontChannelLogoutUrl(),
                applicationProperties.getAuthServer().getPostLogoutRedirectUri(),
                applicationProperties.getAuthServer().getTokenEndpoint(),
                applicationProperties.getAuthServer().getIntrospectionEndpoint(),
                applicationProperties.getAuthServer().getUserInfoEndpoint(),
                applicationProperties.getAuthServer().getEndSessionEndpoint()
        ), new LicenseConfiguration(applicationProperties.getLicenseSpring().getApiKey(),
                applicationProperties.getLicenseSpring().getProductCode(),
                applicationProperties.getLicenseSpring().getSharedKey(),
                applicationProperties.getLicenseSpring().getEnabled()));
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
