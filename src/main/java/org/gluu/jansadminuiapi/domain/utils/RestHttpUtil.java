package org.gluu.jansadminuiapi.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gluu.jansadminuiapi.domain.exceptions.RestCallException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class RestHttpUtil {

    public static void write(HttpServletResponse response, ObjectMapper objectMapper, RestCallException restCallException) throws IOException {
        ResponseEntity<?> re = restCallException.getResponseEntity();
        response.setContentType(Objects.requireNonNull(re.getHeaders().getContentType()).toString());
        response.getWriter().write(objectMapper.writeValueAsString(re.getBody()));
        response.setStatus(re.getStatusCodeValue());
        response.getWriter().flush();
        response.getWriter().close();
    }

    public static void write(HttpServletResponse response, HttpClientErrorException httpClientErrorException) throws IOException {
        response.setContentType(Objects.requireNonNull(Objects.requireNonNull(httpClientErrorException.getResponseHeaders()).getContentType()).toString());
        response.getWriter().write(httpClientErrorException.getResponseBodyAsString());
        response.setStatus(httpClientErrorException.getRawStatusCode());
        response.getWriter().flush();
        response.getWriter().close();
    }

}
