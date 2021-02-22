package org.gluu.adminui.api.services.exception;

import org.gluu.adminui.api.domain.exceptions.ExceptionResponse;
import org.gluu.adminui.api.domain.exceptions.RestCallException;
import org.gluu.adminui.api.domain.utils.ErrorResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = createExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<ExceptionResponse> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {

        ExceptionResponse exceptionResponse = createExceptionResponse(ex.getStatusCode(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, ex.getStatusCode());
    }

    @ExceptionHandler(RestCallException.class)
    public final ResponseEntity<ExceptionResponse> handleRestCallException(RestCallException ex, WebRequest request) {

        ExceptionResponse exceptionResponse = createExceptionResponse(ex.getResponseEntity().getStatusCode(), ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, ex.getResponseEntity().getStatusCode());
    }

    private ExceptionResponse createExceptionResponse(HttpStatus httpStatusCode, String detailsFromServer) {
        ExceptionResponse exceptionResponse = null;
        if (httpStatusCode == HttpStatus.BAD_REQUEST) {
            exceptionResponse = new ExceptionResponse(ErrorResponseCode.BAD_REQUEST_ERROR, detailsFromServer);
        } else if (httpStatusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            exceptionResponse = new ExceptionResponse(ErrorResponseCode.INTERNAL_ERROR_UNKNOWN, detailsFromServer);
        } else if (httpStatusCode == HttpStatus.FORBIDDEN) {
            exceptionResponse = new ExceptionResponse(ErrorResponseCode.FORBIDDEN, detailsFromServer);
        } else if (httpStatusCode == HttpStatus.UNAUTHORIZED) {
            exceptionResponse = new ExceptionResponse(ErrorResponseCode.UNAUTHORIZED, detailsFromServer);
        } else if (httpStatusCode == HttpStatus.METHOD_NOT_ALLOWED) {
            exceptionResponse = new ExceptionResponse(httpStatusCode.value(), httpStatusCode.name(), detailsFromServer);
        } else {
            exceptionResponse = new ExceptionResponse(httpStatusCode.value(), httpStatusCode.name(), detailsFromServer);
        }
        return exceptionResponse;
    }
}
