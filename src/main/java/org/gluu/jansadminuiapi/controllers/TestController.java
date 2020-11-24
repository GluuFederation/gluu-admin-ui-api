package org.gluu.jansadminuiapi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.gluu.jansadminuiapi.SpringConfiguration;
import org.gluu.jansadminuiapi.domain.ws.response.IntrospectionResponse;
import org.pf4j.PluginManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController implements TestControllerInterface {

    @Override
    public String ping() {
        System.out.println("Ping controller");

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        // stop plugins
        PluginManager pluginManager = applicationContext.getBean(PluginManager.class);

        pluginManager.stopPlugins();

        return "pong";
    }

    @Override
    public String testToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("User \"{}\" is correctly authenticated with authorities {}", ((IntrospectionResponse)authentication.getPrincipal()).getUsername(), authentication.getAuthorities());

        return "success";
    }

}
