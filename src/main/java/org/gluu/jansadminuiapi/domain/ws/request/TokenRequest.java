package org.gluu.jansadminuiapi.domain.ws.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequest {
    private String grantType;
    private String code;
    private String redirectUri;
    private String username;
    private String password;
    private List<String> scope;
    private String assertion;
    private String assertionType;
    private String refreshToken;
    private String clientId;
    private String clientSecret;
    private String codeVerifier;
    private String ticket;
    private String claimToken;
    private String claimTokenFormat;
    private String pct;
    private String rpt;
    private String tokenEndpoint;
    private String userInfoJwt;
}
