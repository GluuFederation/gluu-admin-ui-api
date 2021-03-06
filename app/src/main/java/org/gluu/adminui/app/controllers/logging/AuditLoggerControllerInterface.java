package org.gluu.adminui.app.controllers.logging;

import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuditLoggerControllerInterface {
    @PostMapping("/logging/audit")
    ResponseEntity auditLogging(@RequestBody Map<String, Object> loggingRequest, HttpServletRequest request) throws Exception;
}
