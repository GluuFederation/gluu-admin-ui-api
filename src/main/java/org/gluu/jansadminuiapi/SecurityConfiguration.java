package org.gluu.jansadminuiapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.gluu.jansadminuiapi.controllers.OAuth2ControllerInterface;
import org.gluu.jansadminuiapi.services.external.IdPService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Configuration class used to setup security layers.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    private final IdPService idPService;
    private final ObjectMapper objectMapper;
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /*
          1. Disabled cookies usage.
          2. Enabled CORS validation.
          3. CSRF disabled.
          4. Public and protected endpoints configuration.
         */
        httpSecurity
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors()
                .and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.GET, OAuth2ControllerInterface.OAUTH2_CONFIG).permitAll()
                    .antMatchers(HttpMethod.GET, OAuth2ControllerInterface.OAUTH2_ACCESS_TOKEN).permitAll()
                    .antMatchers(HttpMethod.POST, OAuth2ControllerInterface.OAUTH2_API_PROTECTION_TOKEN).permitAll()
                    .anyRequest().authenticated()
                .and().addFilter(new BearerAuthzFilter(authenticationManager(), idPService, objectMapper));
    }

    /**
     * CORS security feature configuration.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*")); // TODO Replace with the right values.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin",
                "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin",
                "Cache-Control", "Content-Type",
                SecurityConfiguration.HEADER_AUTHORIZATION));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers",
                SecurityConfiguration.HEADER_AUTHORIZATION));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
}
