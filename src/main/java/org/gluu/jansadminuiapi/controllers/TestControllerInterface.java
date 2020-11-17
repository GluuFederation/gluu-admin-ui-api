package org.gluu.jansadminuiapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public interface TestControllerInterface {

    @GetMapping("ping")
    String ping();

}
