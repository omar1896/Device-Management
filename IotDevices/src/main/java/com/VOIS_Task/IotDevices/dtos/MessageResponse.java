package com.VOIS_Task.IotDevices.dtos;

import java.time.Instant;
import java.util.List;

public class MessageResponse {
    private String message;
    private Boolean success;
    private Object data;
    private String timestamp;


    public MessageResponse(String message, Boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.timestamp = Instant.now().toString();
    }


    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}