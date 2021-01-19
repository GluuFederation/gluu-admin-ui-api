package org.gluu.jansadminuiapi.controllers;

import org.assertj.core.util.Lists;
import org.gluu.jansadminuiapi.domain.ws.request.TokenRequest;
import org.gluu.jansadminuiapi.domain.ws.response.OAuth2Config;
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
        tokenRequest.setScope(Lists.newArrayList("openid"));

        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/oauth2/api-protection-token",
                tokenRequest,
                String.class)).isNotNull();
    }
}
