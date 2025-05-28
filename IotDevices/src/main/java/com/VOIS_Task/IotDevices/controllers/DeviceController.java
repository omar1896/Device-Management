package com.VOIS_Task.IotDevices.controllers;


import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import com.VOIS_Task.IotDevices.services.DeviceConfigurationServiceInterface;
import com.VOIS_Task.IotDevices.services.DeviceServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/devices")
public class DeviceController implements DeviceControllerInterface {


    private final DeviceServiceInterface deviceService;
    private final DeviceConfigurationServiceInterface deviceConfigurationService;

    @Autowired
    public DeviceController(DeviceServiceInterface deviceService, DeviceConfigurationServiceInterface deviceConfigurationService) {
        this.deviceService = deviceService;
        this.deviceConfigurationService = deviceConfigurationService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createDevice(@RequestBody DeviceRequestDTO deviceRequestDTO) {
        DeviceResponseDTO deviceResponseDTO = deviceService.createDevice(deviceRequestDTO);
        MessageResponse response = new MessageResponse("Device created successfully", true, deviceResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateDevice(@RequestBody DeviceRequestDTO deviceRequestDTO, @PathVariable long id) {
        DeviceResponseDTO deviceResponseDTO = deviceService.updateDevice(deviceRequestDTO, id);
        MessageResponse response = new MessageResponse("Device updated successfully", true, deviceResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteDevice(@PathVariable long id) {
        deviceService.deleteDevice(id);
        MessageResponse response = new MessageResponse("Device deleted successfully", true, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<MessageResponse> getAllAvailableDevices() {
        List<DeviceResponseDTO> devices = deviceService.getAllAvailableDevices();
        MessageResponse response;
        if (devices.isEmpty()) {
            response = new MessageResponse("No devices available", true, null);
        } else {
            response = new MessageResponse("Available devices retrieved successfully", true, devices);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getDeviceById(@PathVariable long id) {
        DeviceResponseDTO responseDTO = deviceService.getDeviceById(id);
        MessageResponse response = new MessageResponse("Device Retrieved Successfully", true, responseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/configure")
    public ResponseEntity<MessageResponse> configureDevice(@PathVariable Long id) {
        DeviceResponseDTO configuredDevice = deviceConfigurationService.configureDevice(id);
        MessageResponse response = new MessageResponse("Device configured successfully", true, configuredDevice);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
