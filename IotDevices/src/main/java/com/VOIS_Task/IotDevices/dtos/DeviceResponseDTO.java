package com.VOIS_Task.IotDevices.dtos;

public class DeviceResponseDTO {

    private Long id;

    private int status;
    private Integer temperature;
    private Boolean availability;

    // Constructors
    public DeviceResponseDTO() {
    }

    public DeviceResponseDTO(Long id, int status, Integer temperature, Boolean availability) {
        this.id = id;
        this.status = status;
        this.temperature = temperature;
        this.availability = availability;
    }

    // Getters

    public void setId(Long id) {
        this.id = id;
    }


    public void setStatus(int status) {
        this.status = status;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Long getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public Boolean getAvailability() {
        return availability;
    }


}