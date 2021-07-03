package org.gluu.adminui.app.controllers.license;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface LicenseControllerInterface {
    String CHECK_LICENSE = "/license/checkLicense";
    String ACTIVATE_LICENSE = "/license/activateLicense";

    @GetMapping(CHECK_LICENSE)
    Boolean checkLicense();

    @GetMapping(ACTIVATE_LICENSE)
    Boolean activateLicense(@RequestParam String licenseKey) throws Exception;
}
