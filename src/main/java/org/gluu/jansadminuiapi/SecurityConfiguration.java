package org.gluu.jansadminuiapi;

import org.gluu.jansadminuiapi.controllers.OAuth2ControllerInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String HEADER_AUTHORIZATION = "Authorization";

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /*
          1. Disabled cookies usage.
          2. Enabled CORS validation.
          3. CSRF disabled.
          4. Public and protected endpoints configuration.
         */
        httpSecurity
				.addFilterAt(new BearerAuthzFilter(authenticationManager), BasicAuthenticationFilter.class)
				//.addFilterAt(new BearerAuthzFilter(), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors()
                .and().csrf().disable().authorizeRequests()
                    .antMatchers(HttpMethod.GET, OAuth2ControllerInterface.OAUTH2_CONFIG).permitAll()
                    .antMatchers(HttpMethod.GET, OAuth2ControllerInterface.OAUTH2_ACCESS_TOKEN).permitAll()
                    .anyRequest().authenticated();
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
