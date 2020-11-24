package org.gluu.jansadminuiapi.services.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gluu.jansadminuiapi.domain.exceptions.RestCallException;
import org.gluu.jansadminuiapi.domain.ws.response.IntrospectionResponse;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdPService {

    private final RestTemplate idPClient;
    private final AppConfiguration appConfiguration;

    /**
     * Calls introspection endpoint from the Identity Provider and returns all fields gotten.
     */
    public IntrospectionResponse introspection(String token) throws RestCallException, HttpClientErrorException {
        try {
            log.trace("Processing token instrospection: {}", token);
            final URI introspectionUri = new URI(this.appConfiguration.getOauth2().getIntrospectionEndpoint());
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("token", token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(body, headers);

            ResponseEntity<IntrospectionResponse> response = idPClient.postForEntity(introspectionUri, request, IntrospectionResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RestCallException(response, "Problems processing token introspection rest call: " + response.getStatusCode());
            }
        } catch (RestCallException | HttpClientErrorException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Problems processing IdP introspection call", e);
            return null;
        }
    }


}
