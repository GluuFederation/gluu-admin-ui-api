package org.gluu.adminui.app.domain.ws.response;

import lombok.Data;

import java.util.Map;

@Data
public class UserInfoResponse {
    private Map<String, Object> claims;
    private String jwtUserInfo;
}
