package com.VOIS_Task.IotDevices.services;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceUpdateRequestDTO;
import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeviceServiceInterface {
    DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO);

    boolean validateDeviceDetails(DeviceRequestDTO deviceRequestDTO) throws DeviceExceptionHandler;

    boolean validateTemperature(DeviceRequestDTO deviceRequestDTO);

    Device findDeviceByPinCode(DeviceRequestDTO deviceRequestDTO);

    DeviceResponseDTO updateDevice(DeviceRequestDTO deviceRequestDTO, Long id) throws DeviceExceptionHandler;

    void transferUpdatedData(DeviceRequestDTO dto, Device existingDevice, Long id) throws DeviceExceptionHandler;

    ResponseEntity<MessageResponse> deleteDevice(Long id);

    List<DeviceResponseDTO> getAllAvailableDevices();
}
