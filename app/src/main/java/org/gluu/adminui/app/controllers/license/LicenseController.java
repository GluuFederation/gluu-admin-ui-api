package org.gluu.adminui.app.controllers.license;

import com.licensespring.License;
import com.licensespring.LicenseManager;
import com.licensespring.LicenseSpringConfiguration;
import com.licensespring.model.ActivationLicense;
import com.licensespring.model.exceptions.LicenseSpringException;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.app.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LicenseController implements LicenseControllerInterface {

    @Autowired
    ApplicationProperties applicationProperties;

    @Override
    public Boolean checkLicense() {
        // configuration with only required parameters
        LicenseSpringConfiguration configuration = LicenseSpringConfiguration.builder()
                .apiKey(applicationProperties.getLicenseSpring().getApiKey())
                .productCode(applicationProperties.getLicenseSpring().getProductCode())
                .sharedKey(applicationProperties.getLicenseSpring().getSharedKey())
                .build();

        LicenseManager licenseManager = LicenseManager.getInstance();

        // initialize the manager once per runtime with the configuration
        try {
            if (!licenseManager.isInitialized()) {
                licenseManager.initialize(configuration);
            }

            License currentLicense = licenseManager.getCurrent();
            if (currentLicense == null) {

                log.info("currentLicense not found");
                return false;
            } else {
                log.info("currentLicense found :: " + currentLicense.getProduct());
            }

        } catch (LicenseSpringException e) {
            log.error(e.getCause().getMessage());
            return false;
        }

        return true;

    }

    @Override
    public Boolean activateLicense(String licenseKey) throws Exception {
        LicenseSpringConfiguration configuration = LicenseSpringConfiguration.builder()
                .apiKey(applicationProperties.getLicenseSpring().getApiKey())
                .productCode(applicationProperties.getLicenseSpring().getProductCode())
                .sharedKey(applicationProperties.getLicenseSpring().getSharedKey())
                .build();

        LicenseManager licenseManager = LicenseManager.getInstance();
        try {
            ActivationLicense keyBased = ActivationLicense.fromKey("GFXJ-AB8B-YDJK-L4AD");
            License license = licenseManager.activateLicense(keyBased);
            License activated = license;
            log.info("currentLicense found :: " + activated.getProduct());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error: ", ex);
        }
        return true;
    }
}
