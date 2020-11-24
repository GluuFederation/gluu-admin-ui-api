package org.gluu.jansadminuiapi.domain.ws.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class IntrospectionResponse {

    @JsonProperty("active")
    private boolean active;

    @JsonProperty("scope")
    private List<String> scope;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("exp")
    private Integer expiresAt;

    @JsonProperty("iat")
    private Integer issuedAt;

    @JsonProperty("sub")
    private String subject;

    @JsonProperty("aud")
    private String audience;

    @JsonProperty("iss")
    private String issuer;

    @JsonProperty("jti")
    private String jti;

    @JsonProperty("acr_values")
    private String acrValues;

}
