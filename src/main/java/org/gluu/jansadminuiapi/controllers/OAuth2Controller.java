package org.gluu.jansadminuiapi.controllers;

import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import lombok.extern.slf4j.Slf4j;
import org.gluu.jansadminuiapi.domain.types.config.OAuth2;
import org.gluu.jansadminuiapi.domain.ws.response.OAuth2Config;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OAuth2Controller implements OAuth2ControllerInterface {

    @Autowired
    private AppConfiguration appConfiguration;

    @Autowired
    private OAuth20Service oAuth20Service;

    @Override
    public OAuth2Config getOAuth2Config() {
        final OAuth2 oAuth2 = appConfiguration.getOauth2();
        OAuth2Config oAuth2Config = new OAuth2Config();
        oAuth2Config.setAuthzBaseUrl(oAuth2.getAuthzBaseUrl());
        oAuth2Config.setClientId(oAuth2.getClientId());
        oAuth2Config.setRedirectUrl(oAuth2.getRedirectUrl());
        oAuth2Config.setScope(oAuth2.getScope());
        oAuth2Config.setResponseType("code");
        oAuth2Config.setAcrValues("simple_password_auth");

        return oAuth2Config;
    }

    @Override
    public ResponseEntity getAccessToken(String code) {
        try {
            OAuth2AccessToken oAuth2AccessToken = oAuth20Service.getAccessToken(code);
            log.info("Access token gotten from IdP: {}", oAuth2AccessToken.getRawResponse());
            return new ResponseEntity(oAuth2AccessToken.getAccessToken(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
