package com.VOIS_Task.IotDevices.entities;

import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_seq")
    @SequenceGenerator(name = "device_seq", sequenceName = "device_sequence", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String pincode;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private DeviceStatus status;

    @Column(nullable = false)
    private Integer temperature;

    @Column(nullable = false)
    private Boolean availability;

    // Constructors
    public Device() {
    }

    public Device(String pincode, DeviceStatus status, Integer temperature, Boolean availability) {
        this.pincode = pincode;
        this.status = status;
        this.temperature = temperature;
        this.availability = availability;
    }

    public Device(long l) {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
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
