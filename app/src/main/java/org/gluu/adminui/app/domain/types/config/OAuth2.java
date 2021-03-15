package org.gluu.adminui.app.domain.types.config;

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
    private String frontChannelLogoutUrl;
    private String postLogoutRedirectUri;
    private String tokenEndpoint;
    private String introspectionEndpoint;
    private String userInfoEndpoint;
    private String endSessionEndpoint;

}
