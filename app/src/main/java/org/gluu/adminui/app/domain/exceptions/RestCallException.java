package org.gluu.adminui.app.domain.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.ResponseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class RestCallException extends Exception {

    private final ResponseEntity<?> responseEntity;

    public RestCallException(ResponseEntity<?> responseEntity, String message) {
        super(message);
        this.responseEntity = responseEntity;
    }

}

