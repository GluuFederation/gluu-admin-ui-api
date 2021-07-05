package org.gluu.adminui.app.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gluu.adminui.app.domain.types.config.LicenseConfiguration;
import org.gluu.adminui.app.domain.types.config.OAuth2;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfiguration {

    private OAuth2 oauth2;
    private LicenseConfiguration licenseConfiguration;
}

