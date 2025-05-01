package com.VOIS_Task.IotDevices.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceRequestDTO {
    @NotNull
    @NotBlank
    @Pattern(regexp = "\\d{7}", message = "Pincode must be exactly 7 digits")
    private String pincode;


    @Min(0)
    @Max(1)
    private int status;

    @NotNull
    @Min(-1)
    @Max(10)
    private Integer temperature;

    private Boolean availability = false;


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
