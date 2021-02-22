package org.gluu.adminui.plugins.hc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class SpringConfiguration {

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
