package org.gluu.jansadminuiapi.domain.types.config;

import com.github.scribejava.core.builder.api.DefaultApi20;
import lombok.AllArgsConstructor;

/**
 * Configuration used by Scribe-java to integrate with third authorization servers.
 */
@AllArgsConstructor
public class IdPApi20 extends DefaultApi20 {

    private final String tokenEndpoint;
    private final String authzBaseUrl;

    @Override
    public String getAccessTokenEndpoint() {
        return tokenEndpoint;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return authzBaseUrl;
    }

}
