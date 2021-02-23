package org.gluu.adminui.app.domain.ws.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2Config {

    private String authzBaseUrl;
    private String clientId;
    private String responseType;
    private String scope;
    private String redirectUrl;
    private String acrValues;

}
