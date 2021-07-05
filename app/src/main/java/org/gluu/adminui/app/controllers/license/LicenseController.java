package org.gluu.adminui.app.controllers.license;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.exceptions.LicenseSpringException;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.app.domain.ws.request.LicenseRequest;
import org.gluu.adminui.app.services.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LicenseController implements LicenseControllerInterface {

    @Autowired
    private AppConfiguration appConfiguration;

    @Override
    public Boolean checkLicense() {
        try {
            License activeLicense = appConfiguration.getLicenseConfiguration().getLicenseManager().getCurrent();
            if (activeLicense == null) {
                log.info("Active license for admin-ui not present ");
                return false;
            } else {
                log.info("Active license for admin-ui found :: " + activeLicense.getProduct());
                return true;
            }
        } catch (LicenseSpringException e) {
            log.error(e.getCause().getMessage());
            return false;
        }
    }

    @Override
    public Boolean activateLicense(LicenseRequest licenseRequest) throws Exception {
        LicenseManager licenseManager = appConfiguration.getLicenseConfiguration().getLicenseManager();
        try {
            ActivationLicense keyBased = ActivationLicense.fromKey(licenseRequest.getLicenseKey());
            License license = licenseManager.activateLicense(keyBased);
            log.info("currentLicense found :: " + license.getProduct());
            return true;
        } catch (LicenseSpringException e) {
            log.error("Error in activating license: ", e);
            return false;
        }

    }
}
