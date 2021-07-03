package org.gluu.adminui.app.controllers.license;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface LicenseControllerInterface {
    String CHECK_LICENSE = "/license/checkLicense";
    String ACTIVATE_LICENSE = "/license/activateLicense";

    @GetMapping(CHECK_LICENSE)
    Boolean checkLicense();

    @PostMapping(ACTIVATE_LICENSE)
    Boolean activateLicense(@RequestBody String licenseKey) throws Exception;
}
