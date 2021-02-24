package org.gluu.adminui.plugins.hc.services;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.api.model.HealthCheck;
import org.gluu.adminui.api.utils.AUIComponents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HealthCheckService {

    @Autowired
    private RestTemplate idPClient;

    @Value("${authServer.healthCheckUrl}")
    private String authServerHCUrl;

    public List<HealthCheck> authServerHealthCheck() throws URISyntaxException {
        final URI healthCheckUri = new URI(authServerHCUrl);
        List<HealthCheck> componentsList = new ArrayList<>();
        try {
            JsonNode authServerStatus = idPClient.getForObject(healthCheckUri, JsonNode.class);

            if (authServerStatus.get("status").toString().replaceAll("\"","").equals("running")) {
                componentsList.add(new HealthCheck(AUIComponents.AUTHORIZATION_SERVER.getValue(), true));
            }

            if (authServerStatus.get("db_status").toString().replaceAll("\"","").equals("online")) {
                componentsList.add(new HealthCheck(AUIComponents.AUTHORIZATION_SERVER_DB.getValue(), true));
            }
            log.info("Health check components: "+componentsList);
            return componentsList;
        } catch (Exception e) {
            log.error("Error in fetching status of Authorization Server: ", e);
            componentsList.add(new HealthCheck(AUIComponents.AUTHORIZATION_SERVER.getValue(), false));
            componentsList.add(new HealthCheck(AUIComponents.AUTHORIZATION_SERVER_DB.getValue(), false));
            return componentsList;
        }
    }

    public void setIdPClient(RestTemplate idPClient) {
        this.idPClient = idPClient;
    }

}
