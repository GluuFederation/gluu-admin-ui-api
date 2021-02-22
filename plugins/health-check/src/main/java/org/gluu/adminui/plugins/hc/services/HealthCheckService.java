package org.gluu.adminui.plugins.hc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HealthCheckService {

    @Autowired
    private RestTemplate idPClient;

    @Value("${authServer.healthCheckUrl}")
    private String authServerHCUrl;

    public String authServerHealthCheck() throws URISyntaxException {
        final URI healthCheckUri = new URI(authServerHCUrl);

        String authServerStatus = idPClient.getForObject(healthCheckUri, String.class);
        return authServerStatus;

    }

    public void setIdPClient(RestTemplate idPClient) {
        this.idPClient = idPClient;
    }

}
