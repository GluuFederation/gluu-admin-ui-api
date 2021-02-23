package org.gluu.adminui.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

public interface PluginControllerInterface {

    @GetMapping("/ping")
    String ping();

    @PreAuthorize("hasAuthority('openid')")
    @GetMapping("/test-token")
    String testToken();

    @GetMapping("/plugins/list")
    ResponseEntity getAllPugins();

}
