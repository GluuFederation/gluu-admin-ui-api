package org.gluu.jansadminuiapi.domain.ws.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class UserInfoResponse {
    private JsonNode claims;

    private String accessToken;

}
