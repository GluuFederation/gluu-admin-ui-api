package org.gluu.adminui.app.controllers.license;

import org.gluu.adminui.app.domain.ws.request.LicenseRequest;
import org.springframework.web.bind.annotation.*;

public interface LicenseControllerInterface {
    String CHECK_LICENSE = "/license/checkLicense";
    String ACTIVATE_LICENSE = "/license/activateLicense";

    @GetMapping(CHECK_LICENSE)
    Boolean checkLicense();

    @PostMapping(ACTIVATE_LICENSE)
    Boolean activateLicense(@RequestBody LicenseRequest licenseRequest) throws Exception;
}
