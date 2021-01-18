package org.gluu.jansadminuiapi.services.external;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.gluu.jansadminuiapi.ApplicationProperties;
import org.gluu.jansadminuiapi.domain.exceptions.RestCallException;
import org.gluu.jansadminuiapi.domain.utils.CommonUtils;
import org.gluu.jansadminuiapi.domain.ws.request.TokenRequest;
import org.gluu.jansadminuiapi.domain.ws.response.IntrospectionResponse;
import org.gluu.jansadminuiapi.domain.ws.response.TokenResponse;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Set;

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

    /**
     * Calls token endpoint from the Identity Provider and returns a valid Access Token.
     */
    public TokenResponse getAccessToken(String code) throws RestCallException, HttpClientErrorException {
        try {
            log.trace("Getting access token with code: {}", code);
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setCode(code);
            return getToken(tokenRequest);

        } catch (RestCallException | HttpClientErrorException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Problems processing IdP token call", e);
            return null;
        }
    }

    /**
     * Calls token endpoint from the Identity Provider and returns a valid Token.
     */
    public TokenResponse getToken(TokenRequest tokenRequest) throws RestCallException, HttpClientErrorException {
        try {
            MultiValueMap<String, String> body = createRequestBody(tokenRequest);
            log.trace("Getting token from auth-server: {}", body.get("redirect_uri"));
            final URI tokenUri = new URI(this.appConfiguration.getOauth2().getTokenEndpoint());
            String clientId = Strings.isNotBlank(tokenRequest.getClientId()) ? tokenRequest.getClientId() : this.appConfiguration.getOauth2().getClientId();
            String clientSecret = Strings.isNotBlank(tokenRequest.getClientSecret()) ? tokenRequest.getClientSecret() : this.appConfiguration.getOauth2().getClientSecret();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(clientId, clientSecret);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<TokenResponse> response = idPClient.postForEntity(tokenUri, request, TokenResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RestCallException(response, "Problems processing token rest call: " + response.getStatusCode());
            }
        } catch (RestCallException | HttpClientErrorException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Problems processing IdP token call", e);
            return null;
        }
    }

    private MultiValueMap<String, String> createRequestBody(TokenRequest tokenRequest) throws Exception {
        try {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            if (Strings.isNotBlank(tokenRequest.getCode())) {
                body.set("code", tokenRequest.getCode());
            }
            if (Strings.isNotBlank(tokenRequest.getGrantType())) {
                body.add("grant_type", tokenRequest.getGrantType());
            } else {
                body.add("grant_type", "client_credentials");
            }
            if (!tokenRequest.getScope().isEmpty()) {
                body.add("scope", scopeAsString(tokenRequest.getScope()));
            }
            if (Strings.isNotBlank(tokenRequest.getRedirectUri())) {
                body.add("redirect_uri", tokenRequest.getRedirectUri());
            } else {
                body.add("redirect_uri", this.appConfiguration.getOauth2().getRedirectUrl());
            }
            if (Strings.isNotBlank(tokenRequest.getAssertion())) {
                body.add("assertion", tokenRequest.getAssertion());
            }
            return body;
        } catch (HttpClientErrorException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Problems processing IdP token call", e);
            return null;
        }
    }

    private static String scopeAsString(List<String> scopes) throws UnsupportedEncodingException {
        Set<String> scope = Sets.newHashSet();
        scope.add("openid");
        scope.addAll(scopes);
        return CommonUtils.joinAndUrlEncode(scope);
    }

}
