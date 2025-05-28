package com.VOIS_Task.IotDevices.mapper;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;

import java.util.Objects;

public class DeviceMapper {

    public static Device toEntity(DeviceRequestDTO dto) {
        Device device = new Device();
        device.setPincode(dto.getPincode());
        device.setTemperature(dto.getTemperature());
        device.setAvailability(dto.getAvailability());
        device.setStatus(DeviceStatus.fromLabel(dto.getStatus()));
        return device;
    }

    public static DeviceResponseDTO toDTO(Device entity) {
        DeviceResponseDTO dto = new DeviceResponseDTO();
        dto.setId(entity.getId());
        dto.setTemperature(entity.getTemperature());
        dto.setAvailability(entity.getAvailability());
        if (Objects.equals(entity.getStatus().getCode(), DeviceStatus.READY.getCode())) {
            dto.setStatus(DeviceStatus.READY.getCode());
        } else {
            dto.setStatus(DeviceStatus.ACTIVE.getCode());
        }
        return dto;
    }
}
