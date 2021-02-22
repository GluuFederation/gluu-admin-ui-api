package org.gluu.adminui.api.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gluu.adminui.api.domain.types.config.OAuth2;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppConfiguration {

    private OAuth2 oauth2;

}
