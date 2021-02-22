package org.gluu.adminui.plugins.hc;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class HealthCheckPluginStarter {
    public static void main(String[] args) {
        SpringApplication.run(HealthCheckPluginStarter.class, args);
    }
}
