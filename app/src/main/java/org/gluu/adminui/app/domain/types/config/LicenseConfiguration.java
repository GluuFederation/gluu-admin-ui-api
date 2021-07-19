package org.gluu.adminui.app.domain.types.config;

import com.licensespring.LicenseManager;
import com.licensespring.LicenseSpringConfiguration;
import com.licensespring.model.exceptions.LicenseSpringException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@NoArgsConstructor
public class LicenseConfiguration {
    String apiKey;
    String productCode;
    String sharedKey;
    Boolean enabled;
    LicenseSpringConfiguration licenseSpringConfiguration;
    LicenseManager licenseManager;

    public LicenseConfiguration(String apiKey, String productCode, String sharedKey, Boolean enabled) {
        this.apiKey = apiKey;
        this.productCode = productCode;
        this.sharedKey = sharedKey;
        this.enabled = enabled;

        if(this.enabled) {
            initializeLicenseManager();
        }
    }

    public void initializeLicenseManager() {
        try {
            this.licenseSpringConfiguration = LicenseSpringConfiguration.builder()
                    .apiKey(apiKey)
                    .productCode(productCode)
                    .sharedKey(sharedKey)
                    .build();

            this.licenseManager = LicenseManager.getInstance();
            if (!licenseManager.isInitialized()) {
                licenseManager.initialize(licenseSpringConfiguration);
            }
        } catch (LicenseSpringException e) {
            log.error("Error in initializing LicenseManager. ", e);
            throw e;
        }
    }
}
