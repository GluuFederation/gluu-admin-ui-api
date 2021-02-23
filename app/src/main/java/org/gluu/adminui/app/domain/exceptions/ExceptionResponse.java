package org.gluu.adminui.app.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gluu.adminui.app.domain.utils.ErrorResponseCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse{
    private int errorCode;
    private String description;
    private String details;

    public ExceptionResponse(ErrorResponseCode errorResponseCode, String details){
        this.errorCode = errorResponseCode.getHttpStatus();
        this.description = errorResponseCode.getDescription();
        this.details = details;
    }
}
