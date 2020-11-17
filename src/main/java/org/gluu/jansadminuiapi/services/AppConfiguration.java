package org.gluu.jansadminuiapi.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gluu.jansadminuiapi.domain.types.config.OAuth2;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfiguration {

    private OAuth2 oauth2;

}

