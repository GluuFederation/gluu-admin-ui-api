package org.gluu.adminui.app.controllers.logging;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gluu.adminui.app.services.external.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuditLoggerController implements AuditLoggerControllerInterface {

    private final RequestService requestService;

    @Override
    public ResponseEntity auditLogging(Map<String, Object> loggingRequest, HttpServletRequest request) throws Exception {
        try {
            String clientIpAddress = requestService.getClientIp(request);
            if (!Strings.isNullOrEmpty(clientIpAddress)) {
                loggingRequest.put("ipAddress", clientIpAddress);
            }
            log.info(loggingRequest.toString());
            return new ResponseEntity("{'status': 'success'}", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Problems in audit logging", e);
            throw e;
        }
    }

}
