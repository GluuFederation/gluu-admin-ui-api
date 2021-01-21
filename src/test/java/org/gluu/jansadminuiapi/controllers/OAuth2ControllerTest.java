package org.gluu.jansadminuiapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.util.Lists;
import org.gluu.jansadminuiapi.domain.ws.request.TokenRequest;
import org.gluu.jansadminuiapi.domain.ws.request.UserInfoRequest;
import org.gluu.jansadminuiapi.domain.ws.response.OAuth2Config;
import org.gluu.jansadminuiapi.domain.ws.response.TokenResponse;
import org.gluu.jansadminuiapi.domain.ws.response.UserInfoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OAuth2ControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getOAuth2Config() {
        OAuth2Config config = this.restTemplate.getForObject("http://localhost:" + port + "/oauth2/config",
                OAuth2Config.class);
        assertThat(config).isNotNull();
        assertThat(config.getAuthzBaseUrl()).isNotNull();
        assertThat(config.getClientId()).isNotNull();
    }

    @Test
    public void getApiProtectionToken() {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setScope(Lists.newArrayList("https://jans.io/oauth/config/openid/clients.readonly"));

        TokenResponse token = this.restTemplate.postForObject("http://localhost:" + port + "/oauth2/api-protection-token",
                tokenRequest,
                TokenResponse.class);
        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getScope()).isNotNull();
    }

    @Test
    public void getAccessToken() {
        TokenResponse token = this.restTemplate.getForObject("http://localhost:" + port + "/oauth2/access-token?code=8a6559ef-9347-40c3-a4b4-74b98c45894c",
                TokenResponse.class);

        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getIdToken()).isNotNull();
    }

    @Test
    public void getUserInfo() {
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setCode("ea4b7c66-dea9-490f-b596-da0cd25c230e");
        UserInfoResponse userInfo = this.restTemplate.postForObject("http://localhost:" + port + "/oauth2/user-info",
                userInfoRequest,
                UserInfoResponse.class);

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getAccessToken()).isNotNull();
        assertThat(userInfo.getClaims()).isNotNull();
    }
}
