package org.gluu.adminui.api.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

public interface TestControllerInterface {

    @GetMapping("/ping")
    String ping();

    @PreAuthorize("hasAuthority('openid')")
    @GetMapping("/test-token")
    String testToken();

}
