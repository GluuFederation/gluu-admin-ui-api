package org.gluu.adminui.app.controllers;

import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.api.plugins.IAdminUIPlugin;
import org.gluu.adminui.app.SpringConfiguration;
import org.gluu.adminui.app.domain.ws.response.IntrospectionResponse;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PluginController implements PluginControllerInterface {

    @Autowired
    private PluginManager pluginManager;

    public String ping() {
        System.out.println("Ping controllers");

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        // stop plugins
        //PluginManager pluginManager = applicationContext.getBean(PluginManager.class);
        List<PluginWrapper> resolvedPlugins = pluginManager.getResolvedPlugins();
        pluginManager.stopPlugins();

        return "pong";
    }

    public String testToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("User \"{}\" is correctly authenticated with authorities {}", ((IntrospectionResponse) authentication.getPrincipal()).getUsername(), authentication.getAuthorities());

        return "success";
    }

    public ResponseEntity getAllPugins() {

        List<IAdminUIPlugin> registers = pluginManager.getExtensions(IAdminUIPlugin.class);
        return new ResponseEntity(registers, HttpStatus.OK);
    }

}
