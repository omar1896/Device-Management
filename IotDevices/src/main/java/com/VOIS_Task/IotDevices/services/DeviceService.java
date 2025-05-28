package com.VOIS_Task.IotDevices.services;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import com.VOIS_Task.IotDevices.entities.Device;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.exceptionhandler.DeviceExceptionHandler;
import com.VOIS_Task.IotDevices.mapper.DeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;

import java.util.List;


@Service
public class DeviceService implements DeviceServiceInterface {
    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceResponseDTO createDevice(DeviceRequestDTO deviceRequestDTO) {
        Device newDevice = new Device();
        if (validateMandatoryFields(deviceRequestDTO) && validateTemperatureAndStatus(deviceRequestDTO)) {
            newDevice = DeviceMapper.toEntity(deviceRequestDTO);
            deviceRepository.save(newDevice);
        }
        return DeviceMapper.toDTO(newDevice);
    }


    public boolean validateMandatoryFields(DeviceRequestDTO deviceRequestDTO) throws DeviceExceptionHandler {
        if (deviceRequestDTO.getPincode() == null || deviceRequestDTO.getTemperature() == null) {
            throw new DeviceExceptionHandler("device Temperature,Status and Pin code is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (deviceRepository.getByPincode(deviceRequestDTO.getPincode()) != null) {
            throw new DeviceExceptionHandler("Pincode already exists", HttpStatus.BAD_REQUEST);
        }
        return true;
    }


    public boolean validateTemperatureAndStatus(DeviceRequestDTO deviceRequestDTO) {

        if (deviceRequestDTO.getTemperature() >= 0 && deviceRequestDTO.getStatus().equals(DeviceStatus.READY.getLabel())) {
            throw new DeviceExceptionHandler("Inactive(Ready) devices can't have temperature > 0", HttpStatus.BAD_REQUEST);
        }
        if (deviceRequestDTO.getTemperature() < 0 && deviceRequestDTO.getStatus().equals(DeviceStatus.ACTIVE.getLabel())) {
            throw new DeviceExceptionHandler("Active devices can't have temperature < 0", HttpStatus.BAD_REQUEST);
        }
        return true;
    }


    public DeviceResponseDTO updateDevice(DeviceRequestDTO deviceRequestDTO, Long id) throws DeviceExceptionHandler {
        Device existingDevice = deviceRepository.findById(id).orElseThrow(() -> new DeviceExceptionHandler("DEVICE_DOES_NOT_EXIST", HttpStatus.BAD_REQUEST));
        transferUpdatedData(deviceRequestDTO, existingDevice, id);
        deviceRepository.save(existingDevice);
        return DeviceMapper.toDTO(existingDevice);
    }

    public void validateStatusAndTemperatureUpdate(Device existingDevice, DeviceRequestDTO updateDto) {
        Integer temp = updateDto.getTemperature() != null ? updateDto.getTemperature() : existingDevice.getTemperature();
        Integer status = updateDto.getStatus() != null ? DeviceStatus.fromLabel(updateDto.getStatus()).getCode() : existingDevice.getStatus().getCode();

        if (status == 1 && temp < 0 || status == 1 && temp > 10) {
            throw new DeviceExceptionHandler("Active devices can't have temperature < 0 or > 10 ", HttpStatus.BAD_REQUEST);
        }
        if (status == 0 && temp != -1) {
            throw new DeviceExceptionHandler("Inactive devices can have temperature -1 only ", HttpStatus.BAD_REQUEST);
        }
    }

    public void transferUpdatedData(DeviceRequestDTO dto, Device existingDevice, Long id) throws DeviceExceptionHandler {
        if (dto.getAvailability() != null) {
            existingDevice.setAvailability(dto.getAvailability());
        }
        if (dto.getStatus() != null) {
            validateStatusAndTemperatureUpdate(existingDevice, dto);
            existingDevice.setStatus(DeviceStatus.fromLabel(dto.getStatus()));
        }
        if (dto.getTemperature() != null) {
            validateStatusAndTemperatureUpdate(existingDevice, dto);
            existingDevice.setTemperature(dto.getTemperature());
        }
        if (dto.getPincode() != null && !dto.getPincode().isBlank()) {
            Device foundByPin = deviceRepository.getByPincode(dto.getPincode());
            if (foundByPin != null && !foundByPin.getId().equals(id)) {
                throw new DeviceExceptionHandler("Pin Code Already Exist", HttpStatus.BAD_REQUEST);
            }
            existingDevice.setPincode(dto.getPincode());
        }
    }


    public ResponseEntity<MessageResponse> deleteDevice(Long id) {
        Device existingDevice = deviceRepository.findById(id).orElseThrow(() -> new DeviceExceptionHandler("DEVICE_DOES_NOT_EXIST", HttpStatus.BAD_REQUEST));
        deviceRepository.delete(existingDevice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<DeviceResponseDTO> getAllAvailableDevices() {
        List<Device> devices = deviceRepository.findAllByAvailabilityTrueOrderByPincodeAsc();
        return devices.stream()
                .map(DeviceMapper::toDTO)
                .toList();
    }

    public DeviceResponseDTO getDeviceById(Long id) {
        Device existingDevice = deviceRepository.findById(id).orElseThrow(() -> new DeviceExceptionHandler("DEVICE_DOES_NOT_EXIST", HttpStatus.BAD_REQUEST));
        return DeviceMapper.toDTO(existingDevice);
    }
}
