package com.VOIS_Task.IotDevices.exceptionhandler;

import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DeviceExceptionHandler.class)
    public ResponseEntity<MessageResponse> handleDeviceException(DeviceExceptionHandler ex) {
        MessageResponse response = new MessageResponse(
                ex.getMessage(),
                false,
                null
        );
        return new ResponseEntity<>(response, ex.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation error(s): ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(String.format("[%s: %s] ", error.getField(), error.getDefaultMessage()));
        }

        MessageResponse response = new MessageResponse(
                errorMessage.toString(),
                false,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        MessageResponse response = new MessageResponse(
                ex.getMessage(),
                false,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Optional: Handle all other uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGenericException(Exception ex) {
        MessageResponse response = new MessageResponse(
                ex.getMessage(),
                false,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
