package com.VOIS_Task.IotDevices.exceptionhandler;

import org.springframework.http.HttpStatus;

public class DeviceExceptionHandler extends RuntimeException {
    private final HttpStatus code;



    public DeviceExceptionHandler(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return this.code;
    }
}
