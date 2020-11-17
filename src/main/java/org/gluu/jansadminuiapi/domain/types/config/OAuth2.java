package org.gluu.jansadminuiapi.domain.types.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2 {

    private String authzBaseUrl;
    private String clientId;
    private String clientSecret;
    private String scope;
    private String redirectUrl;
    private String logoutRedirectUrl;
    private String tokenEndpoint;

}
