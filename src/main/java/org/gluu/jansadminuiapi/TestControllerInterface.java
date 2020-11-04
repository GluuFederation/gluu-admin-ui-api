package org.gluu.jansadminuiapi;

import org.springframework.web.bind.annotation.GetMapping;

public interface TestControllerInterface {

    @GetMapping("ping")
    String ping();

}
