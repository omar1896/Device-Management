package com.VOIS_Task.IotDevices.services;

import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;

public interface DeviceConfigurationServiceInterface {
    public DeviceResponseDTO configureDevice(Long id) throws DeviceExceptionHandler;
}
