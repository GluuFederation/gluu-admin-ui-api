package org.gluu.adminui.api.domain.ws.response;

import lombok.Data;

import java.util.Map;

@Data
public class UserInfoResponse {
    private Map<String, Object> claims;
    private String jwtUserInfo;
}
