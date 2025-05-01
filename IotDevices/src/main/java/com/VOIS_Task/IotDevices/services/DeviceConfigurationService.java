package com.VOIS_Task.IotDevices.services;

import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;
import com.VOIS_Task.IotDevices.mapper.DeviceMapper;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DeviceConfigurationService implements DeviceConfigurationServiceInterface {
    private final DeviceRepository deviceRepository;
    private final Random random = new Random();

    @Autowired
    public DeviceConfigurationService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceResponseDTO configureDevice(Long id) throws DeviceExceptionHandler {
        Device existingDevice = deviceRepository.findById(id).orElseThrow(() -> new DeviceExceptionHandler("DEVICE_DOES_NOT_EXIST", HttpStatus.BAD_REQUEST));
        if (isAlreadyActivated(existingDevice)) {
            throw new DeviceExceptionHandler("Device Already Activated", HttpStatus.OK);
        }
        if (isUnavailable(existingDevice)) {
            throw new DeviceExceptionHandler("Device Is Not Available", HttpStatus.BAD_REQUEST);

        }
        updateAndSaveDevice(existingDevice);
        return DeviceMapper.toDTO(existingDevice);
    }

    public boolean isAlreadyActivated(Device existingDevice) {
        return existingDevice.getStatus().equals(DeviceStatus.ACTIVE);
    }

    public boolean isUnavailable(Device existingDevice) {
        return !existingDevice.getAvailability();
    }

    public void updateAndSaveDevice(Device existingDevice) {
        existingDevice.setStatus(DeviceStatus.ACTIVE);
        existingDevice.setTemperature(random.nextInt(11));
        deviceRepository.save(existingDevice);
    }
}
