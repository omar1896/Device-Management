package com.VOIS_Task.IotDevices.services;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DeviceServiceInterface {
    DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO);

    boolean validateMandatoryFields(DeviceRequestDTO deviceRequestDTO) throws DeviceExceptionHandler;


    DeviceResponseDTO updateDevice(DeviceRequestDTO deviceRequestDTO, Long id) throws DeviceExceptionHandler;

    void transferUpdatedData(DeviceRequestDTO dto, Device existingDevice, Long id) throws DeviceExceptionHandler;

    ResponseEntity<MessageResponse> deleteDevice(Long id);

    List<DeviceResponseDTO> getAllAvailableDevices();

      DeviceResponseDTO getDeviceById(Long id);
}
