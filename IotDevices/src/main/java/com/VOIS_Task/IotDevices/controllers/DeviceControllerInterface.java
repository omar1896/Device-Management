package com.VOIS_Task.IotDevices.controllers;

import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface DeviceControllerInterface {
    ResponseEntity<MessageResponse> createDevice(@Valid @RequestBody DeviceRequestDTO deviceRequestDTO);

    ResponseEntity<MessageResponse> updateDevice(@RequestBody DeviceRequestDTO deviceRequestDTO, @PathVariable long id);

    ResponseEntity<MessageResponse> deleteDevice(@PathVariable long id);

    ResponseEntity<MessageResponse> getAllAvailableDevices();

    ResponseEntity<MessageResponse> getDeviceById(@PathVariable long id);

    ResponseEntity<MessageResponse> configureDevice(@PathVariable Long id);
}
