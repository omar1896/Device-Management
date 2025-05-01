package com.VOIS_Task.IotDevices.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceUpdateRequestDTO {

//    @Pattern(regexp = "\\d{7}", message = "Pincode must be exactly 7 digits")
    private String pincode;

    private Integer status;

    private Integer temperature;

    private Boolean availability;

    public String getPincode() {
        return pincode;
    }

    public void setPincode( String pincode) {
        this.pincode = pincode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}