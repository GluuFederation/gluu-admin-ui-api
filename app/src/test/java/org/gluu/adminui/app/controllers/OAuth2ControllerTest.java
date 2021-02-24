package org.gluu.adminui.app.controllers;

import org.assertj.core.util.Lists;
import org.gluu.adminui.app.ApplicationProperties;
import org.gluu.adminui.app.domain.ws.request.UserInfoRequest;
import org.gluu.adminui.app.domain.ws.response.OAuth2Config;
import org.gluu.adminui.app.domain.ws.response.TokenResponse;
import org.gluu.adminui.app.domain.ws.response.UserInfoResponse;
import org.gluu.adminui.app.util.SeleniumTestUtils;
import org.gluu.adminui.app.util.TestUtil;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OAuth2ControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    private String TEST_SERVER_HOST = "http://localhost";

    private String ujwt = null;

    @Test
    public void A_getOAuth2Config() throws Exception {
        OAuth2Config config = this.restTemplate.getForObject(TEST_SERVER_HOST + ":" + port + OAuth2ControllerInterface.OAUTH2_CONFIG,
                OAuth2Config.class);
        assertThat(config).isNotNull();
        assertThat(config.getAuthzBaseUrl()).isNotNull();
        assertThat(config.getClientId()).isNotNull();
    }

    @Test
    public void B_getAccessToken() throws IOException, URISyntaxException {

        String code = getCode();
        TokenResponse token = this.restTemplate.getForObject(TEST_SERVER_HOST + ":" + port + OAuth2ControllerInterface.OAUTH2_ACCESS_TOKEN + "?code=" + code,
                TokenResponse.class);

        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isNotNull();
        assertThat(token.getIdToken()).isNotNull();
    }

    @Test
    public void C_getUserInfo() throws IOException, URISyntaxException {
        String code = getCode();
        UserInfoRequest userInfoRequest = new UserInfoRequest();
        userInfoRequest.setCode(code);
        UserInfoResponse userInfo = this.restTemplate.postForObject("http://localhost:" + port + "/oauth2/user-info",
                userInfoRequest,
                UserInfoResponse.class);

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getJwtUserInfo()).isNotNull();
        assertThat(userInfo.getClaims()).isNotNull();
        ujwt = userInfo.getJwtUserInfo();
    }

    @Test
    public void D_getApiProtectionToken() {

        TokenResponse token = this.restTemplate.getForObject(TEST_SERVER_HOST + ":" + port + OAuth2ControllerInterface.OAUTH2_API_PROTECTION_TOKEN + "?ujwt=" + ujwt,
                TokenResponse.class);
        assertThat(token).isNotNull();
        assertThat(token.getAccessToken()).isNotNull();

    }

    private String getCode() throws IOException, URISyntaxException {
        final String state = UUID.randomUUID().toString();
        final String nonce = UUID.randomUUID().toString();

        String redirectUrl = SeleniumTestUtils.authorizeClient(applicationProperties.getAuthServer().getHost(),
                applicationProperties.getAuthServer().getUsername(),
                applicationProperties.getAuthServer().getPassword(),
                applicationProperties.getAuthServer().getClientId(),
                applicationProperties.getAuthServer().getRedirectUrl(),
                state, nonce,
                Lists.newArrayList("code"),
                Lists.newArrayList(applicationProperties.getAuthServer().getScope().split("\\+")));

        Map<String, String> paramsMap = TestUtil.getParamsMapFromRedirectUrl(redirectUrl);
        return paramsMap.get("code");
    }
}
