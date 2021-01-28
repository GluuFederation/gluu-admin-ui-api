package org.gluu.jansadminuiapi.services.external;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.jans.as.model.jwt.Jwt;
import io.jans.as.model.jwt.JwtClaims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.gluu.jansadminuiapi.ApplicationProperties;
import org.gluu.jansadminuiapi.domain.exceptions.RestCallException;
import org.gluu.jansadminuiapi.domain.utils.CommonUtils;
import org.gluu.jansadminuiapi.domain.ws.request.TokenRequest;
import org.gluu.jansadminuiapi.domain.ws.request.UserInfoRequest;
import org.gluu.jansadminuiapi.domain.ws.response.IntrospectionResponse;
import org.gluu.jansadminuiapi.domain.ws.response.TokenResponse;
import org.gluu.jansadminuiapi.domain.ws.response.UserInfoResponse;
import org.gluu.jansadminuiapi.services.AppConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdPService {

    private final RestTemplate idPClient;
    private final AppConfiguration appConfiguration;

    @Autowired
    private final ApplicationProperties applicationProperties;

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
            tokenRequest.setGrantType("authorization_code");
            tokenRequest.setClientId(this.appConfiguration.getOauth2().getClientId());
            tokenRequest.setClientSecret(this.appConfiguration.getOauth2().getClientSecret());
            tokenRequest.setTokenEndpoint(this.appConfiguration.getOauth2().getTokenEndpoint());
            tokenRequest.setRedirectUri(this.appConfiguration.getOauth2().getRedirectUrl());
            tokenRequest.setScope(Lists.newArrayList(this.appConfiguration.getOauth2().getScope().split("\\+")));
            return getToken(tokenRequest);

        } catch (RestCallException | HttpClientErrorException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Problems processing IdP token call", e);
            return null;
        }
    }

    /**
     * Calls token endpoint from the Identity Provider and returns a valid Access Token.
     */
    public TokenResponse getApiProtectionToken(String userInfoJwt) throws RestCallException, HttpClientErrorException {
        try {
            log.trace("Getting access token with userInfoJwt: {}", userInfoJwt);
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setGrantType("client_credentials");
            tokenRequest.setClientId(this.applicationProperties.getTokenServer().getClientId());
            tokenRequest.setClientSecret(this.applicationProperties.getTokenServer().getClientSecret());
            tokenRequest.setTokenEndpoint(this.applicationProperties.getTokenServer().getTokenEndpoint());
            tokenRequest.setRedirectUri(this.applicationProperties.getTokenServer().getRedirectUrl());
            tokenRequest.setUserInfoJwt(userInfoJwt);
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
            final URI tokenUri = new URI(tokenRequest.getTokenEndpoint());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(tokenRequest.getClientId(), tokenRequest.getClientSecret());

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

    /**
     * Fetches user-info from authorization server .
     */
    public UserInfoResponse getUserInfo(UserInfoRequest userInfoRequest) {
        try {
            log.trace("Getting User-Info from auth-server: {}", userInfoRequest.getAccess_token());
            final URI userInfoUri = new URI(this.appConfiguration.getOauth2().getUserInfoEndpoint());

            String accessToken = Strings.isNotBlank(userInfoRequest.getAccess_token()) ? userInfoRequest.getAccess_token() : null;

            if (Strings.isBlank(userInfoRequest.getCode()) && Strings.isBlank(accessToken)) {
                log.error("Bad Request: Either `code` or `access_token` is required.");
                throw new Exception("Bad Request: Either `code` or `access_token` is required.");
            }

            if (Strings.isNotBlank(userInfoRequest.getCode()) && Strings.isBlank(accessToken)) {
                TokenResponse tokenResponse = getAccessToken(userInfoRequest.getCode());
                accessToken = tokenResponse.getAccessToken();
            }


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(accessToken);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.set("access_token", accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = idPClient.postForEntity(userInfoUri, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                final Jwt jwtUserInfo = Jwt.parse(response.getBody());
                UserInfoResponse userInfoResponse = setClaims(jwtUserInfo);
                userInfoResponse.setJwtUserInfo(response.getBody());
                userInfoResponse.setAccessToken(accessToken);
                return userInfoResponse;
            } else {
                throw new RestCallException(response, "Problems processing user-info rest call: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException exception) {
            throw exception;
        } catch (Exception e) {
            log.error("Problems processing IdP token call", e);
            return null;
        }
    }

    private UserInfoResponse setClaims(Jwt jwtUserInfo) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        JwtClaims jwtClaims = jwtUserInfo.getClaims();
        Map<String, Object> claims = Maps.newHashMap();
        Set<String> keys = jwtClaims.keys();
        keys.forEach((key) -> {

            if (jwtClaims.getClaim(key) instanceof String)
                claims.put(key, jwtClaims.getClaim(key).toString());

            else if (jwtClaims.getClaim(key) instanceof JSONArray) {
                List<String> sourceArr = jwtClaims.getClaimAsStringList(key);
                claims.put(key, sourceArr);
            } else if (jwtClaims.getClaim(key) instanceof JSONObject)
                claims.put(key, ((JSONObject) jwtClaims.getClaim(key)));
        });

        userInfoResponse.setClaims(claims);
        return userInfoResponse;
    }

    private MultiValueMap<String, String> createRequestBody(TokenRequest tokenRequest) throws Exception {
        try {
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            if (Strings.isNotBlank(tokenRequest.getCode())) {
                body.set("code", tokenRequest.getCode());
            }
            if (Strings.isNotBlank(tokenRequest.getGrantType())) {
                body.add("grant_type", tokenRequest.getGrantType());
            }
            if (tokenRequest.getScope() != null && !tokenRequest.getScope().isEmpty()) {
                body.add("scope", scopeAsString(tokenRequest.getScope()));
            }
            if (Strings.isNotBlank(tokenRequest.getRedirectUri())) {
                body.add("redirect_uri", tokenRequest.getRedirectUri());
            }
            if (Strings.isNotBlank(tokenRequest.getRedirectUri())) {
                body.add("redirect_uri", tokenRequest.getRedirectUri());
            }
            if (Strings.isNotBlank(tokenRequest.getUserInfoJwt())) {
                body.add("ujwt", tokenRequest.getUserInfoJwt());
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
        scope.addAll(scopes);
        return CommonUtils.joinAndUrlEncode(scope);
    }
}
