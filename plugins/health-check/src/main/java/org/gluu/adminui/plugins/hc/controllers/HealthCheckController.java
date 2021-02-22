package org.gluu.adminui.plugins.hc.controllers;

import org.gluu.adminui.plugins.hc.services.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/plugins")
public class HealthCheckController {

    @Autowired
    HealthCheckService healthCheckService;

    @GetMapping("/health-check")
    public String plugin() throws URISyntaxException {
        return healthCheckService.authServerHealthCheck();
    }

}
