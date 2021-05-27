package org.gluu.adminui.app.controllers.logging;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuditLoggerController implements AuditLoggerControllerInterface {
    @Override
    public ResponseEntity auditLogging(Map<String, Object> loggingRequest) throws Exception {
        try {
            log.info(loggingRequest.toString());
            return new ResponseEntity("{'status': 'success'}", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems in audit logging", e);
            throw e;
        }
    }

}
