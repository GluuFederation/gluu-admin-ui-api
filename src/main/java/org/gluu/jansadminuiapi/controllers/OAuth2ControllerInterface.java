package org.gluu.jansadminuiapi.controllers;

import org.gluu.jansadminuiapi.domain.ws.request.TokenRequest;
import org.gluu.jansadminuiapi.domain.ws.response.OAuth2Config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Defines all endpoints used to process OAuth2 flow.
 */
public interface OAuth2ControllerInterface {

    String OAUTH2_CONFIG = "/oauth2/config";
    String OAUTH2_ACCESS_TOKEN = "/oauth2/access-token";
    String OAUTH2_API_PROTECTION_TOKEN = "/oauth2/api-protection-token";

    @GetMapping(OAUTH2_CONFIG)
    OAuth2Config getOAuth2Config();

    @GetMapping(OAUTH2_ACCESS_TOKEN)
    ResponseEntity getAccessToken(@RequestParam String code);

    @PostMapping(OAUTH2_API_PROTECTION_TOKEN)
    ResponseEntity getApiProtectionToken(@RequestBody TokenRequest tokenRequest);

}
