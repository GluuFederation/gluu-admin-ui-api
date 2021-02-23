package org.gluu.adminui.app.domain.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.StringUtils;

public enum ErrorResponseCode {
    INTERNAL_ERROR_UNKNOWN(500, "internal_error", "Unknown internal server error occurs."),
    BAD_REQUEST_ERROR(400, "bad_request", "Request parameters are not specified or otherwise malformed/invalid."),
    FORBIDDEN(403, "forbidden", "The access to the resource is forbidden."),
    UNAUTHORIZED(401, "unauthorized", "The client is not authorized to access the resource.");

    private final int httpStatus;
    private final String code;
    private final String description;

    ErrorResponseCode(int httpStatus, String code, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDescription() {
        return description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static ErrorResponseCode fromValue(String v) {
        if (StringUtils.isNotBlank(v)) {
            for (ErrorResponseCode t : values()) {
                if (t.getCode().equalsIgnoreCase(v)) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ErrorResponseCode");
        sb.append("{value='").append(code).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
