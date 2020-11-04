package org.gluu.adminapi;

import org.springframework.web.bind.annotation.GetMapping;

public interface TestControllerInterface {

    @GetMapping("ping")
    String ping();

}
