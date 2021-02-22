package org.gluu.adminui.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main Spring Boot application.
 */
@SpringBootApplication
public class SpringApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
    }

    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        setRegisterErrorPageFilter(false);
        return application.sources(SpringApp.class);
    }*/

}
