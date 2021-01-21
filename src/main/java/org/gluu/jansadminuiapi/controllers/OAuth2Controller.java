package org.gluu.jansadminuiapi.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gluu.jansadminuiapi.domain.types.config.OAuth2;
import org.gluu.jansadminuiapi.domain.ws.request.TokenRequest;
import org.gluu.jansadminuiapi.domain.ws.request.UserInfoRequest;
import org.gluu.jansadminuiapi.domain.ws.response.OAuth2Config;
import org.gluu.jansadminuiapi.domain.ws.response.TokenResponse;
import org.gluu.jansadminuiapi.domain.ws.response.UserInfoResponse;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.gluu.jansadminuiapi.services.external.IdPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuth2Controller implements OAuth2ControllerInterface {


    private final AppConfiguration appConfiguration;
    private final IdPService idPService;

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
            TokenResponse tokenResponse = idPService.getAccessToken(code);
            log.info("Access token gotten from IdP: {}", tokenResponse.toString());
            return new ResponseEntity(tokenResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity getUserInfo(UserInfoRequest userInfoRequest) {
        try {
            log.info("Get User-Info request to IdP: {}", userInfoRequest.toString());
            UserInfoResponse userInfoResponse = idPService.getUserInfo(userInfoRequest);
            log.info("Get User-Info gotten from IdP: {}", userInfoResponse.toString());
            return new ResponseEntity(userInfoResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity getApiProtectionToken(TokenRequest tokenRequest) {
        try {
            log.info("Api protection token request to IdP: {}", tokenRequest.toString());
            TokenResponse tokenResponse = idPService.getToken(tokenRequest);
            log.info("Api protection token gotten from IdP: {}", tokenResponse.toString());
            return new ResponseEntity(tokenResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
