package org.gluu.adminui.app.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.app.domain.types.config.OAuth2;
import org.gluu.adminui.app.domain.ws.request.UserInfoRequest;
import org.gluu.adminui.app.domain.ws.response.OAuth2Config;
import org.gluu.adminui.app.domain.ws.response.TokenResponse;
import org.gluu.adminui.app.domain.ws.response.UserInfoResponse;
import org.gluu.adminui.app.services.AppConfiguration;
import org.gluu.adminui.app.services.external.IdPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        oAuth2Config.setFrontChannelLogoutUrl(oAuth2.getFrontChannelLogoutUrl());
        oAuth2Config.setPostLogoutRedirectUri(oAuth2.getPostLogoutRedirectUri());
        oAuth2Config.setEndSessionEndpoint(oAuth2.getEndSessionEndpoint());

        return oAuth2Config;
    }

    @Override
    public ResponseEntity getAccessToken(String code) throws Exception {
        try {
            log.info("Access token request to IdP.");
            TokenResponse tokenResponse = idPService.getAccessToken(code);
            log.info("Access token gotten from IdP: {}", tokenResponse.toString());
            return new ResponseEntity(tokenResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity getUserInfo(UserInfoRequest userInfoRequest) throws Exception {
        try {
            log.info("Get User-Info request to IdP: {}", userInfoRequest.toString());
            UserInfoResponse userInfoResponse = idPService.getUserInfo(userInfoRequest);
            log.info("Get User-Info gotten from IdP: {}", userInfoResponse.toString());
            return new ResponseEntity(userInfoResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            throw e;
        }
    }

    @Override
    public ResponseEntity getApiProtectionToken(String ujwt) throws Exception {
        try {
            log.info("Api protection token request to IdP.");
            TokenResponse tokenResponse = idPService.getApiProtectionToken(ujwt);
            log.info("Api protection token gotten from IdP: {}", tokenResponse.toString());
            return new ResponseEntity(tokenResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems getting access token", e);
            throw e;
        }
    }

}
