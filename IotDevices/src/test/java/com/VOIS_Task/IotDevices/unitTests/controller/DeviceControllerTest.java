package com.VOIS_Task.IotDevices.unitTests.controller;


import com.VOIS_Task.IotDevices.controllers.DeviceController;
import com.VOIS_Task.IotDevices.dtos.DeviceRequestDTO;
import com.VOIS_Task.IotDevices.dtos.DeviceResponseDTO;
import com.VOIS_Task.IotDevices.dtos.MessageResponse;
import com.VOIS_Task.IotDevices.enumerators.DeviceStatus;
import com.VOIS_Task.IotDevices.repository.DeviceRepository;
import com.VOIS_Task.IotDevices.services.DeviceConfigurationServiceInterface;
import com.VOIS_Task.IotDevices.services.DeviceServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class DeviceControllerTest {
    @Mock
    private DeviceRepository DeviceRepository;
    @Mock
    private DeviceServiceInterface deviceService;
    @Mock
    private DeviceConfigurationServiceInterface deviceConfigurationService;
    @InjectMocks
    private DeviceController deviceController;


    @Test
    void testCreateDevice() {
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setTemperature(5);
        request.setStatus(DeviceStatus.ACTIVE.getLabel());
        DeviceResponseDTO responseDTO = new DeviceResponseDTO();
        responseDTO.setTemperature(5);
        responseDTO.setStatus(1);

        when(deviceService.createDevice(request)).thenReturn(responseDTO);

        ResponseEntity<MessageResponse> response = deviceController.createDevice(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Device created successfully");
        assertThat(response.getBody().getData()).isEqualTo(responseDTO);
        verify(deviceService, times(1)).createDevice(request);
    }

    @Test
    void testUpdateDevice() {
        long id = 1L;
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setTemperature(5);
        DeviceResponseDTO responseDTO = new DeviceResponseDTO();
        responseDTO.setTemperature(5);

        when(deviceService.updateDevice(request, id)).thenReturn(responseDTO);

        ResponseEntity<MessageResponse> response = deviceController.updateDevice(request, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Device updated successfully");
        assertThat(response.getBody().getData()).isEqualTo(responseDTO);
        verify(deviceService, times(1)).updateDevice(request, id);
    }
    @Test
    void testUpdateDeviceFailureDueToInvalidStatus() {
        long id = 1L;
        DeviceRequestDTO request = new DeviceRequestDTO();
        request.setTemperature(5);

        when(deviceService.updateDevice(request, id))
                .thenThrow(new IllegalArgumentException("Device status is invalid for update"));

        assertThatThrownBy(() -> deviceController.updateDevice(request, id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Device status is invalid for update");

        verify(deviceService, times(1)).updateDevice(request, id);
    }

    @Test
    void testDeleteDevice() {
        long id = 8L;
        ResponseEntity<MessageResponse> response = deviceController.deleteDevice(id);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage()).isEqualTo("Device deleted successfully");
        assertThat(response.getBody().getData()).isNull();
        verify(deviceService, times(1)).deleteDevice(id);
    }

    @Test
    void testGetAllAvailableDevices_WhenDevicesExist() {
        DeviceResponseDTO dto1 = new DeviceResponseDTO();
        DeviceResponseDTO dto2 = new DeviceResponseDTO();
        List<DeviceResponseDTO> devices = Arrays.asList(dto1, dto2);

        when(deviceService.getAllAvailableDevices()).thenReturn(devices);

        ResponseEntity<MessageResponse> response = deviceController.getAllAvailableDevices();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).isEqualTo(devices);
        assertThat(response.getBody().getMessage()).isEqualTo("Available devices retrieved successfully");
    }

    @Test
    void testGetAllAvailableDevices_WhenNoDevices() {
        when(deviceService.getAllAvailableDevices()).thenReturn(Collections.emptyList());

        ResponseEntity<MessageResponse> response = deviceController.getAllAvailableDevices();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("No devices available");
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    void testConfigureDevice() {
        Long id = 1L;
        DeviceResponseDTO responseDTO = new DeviceResponseDTO();
        when(deviceConfigurationService.configureDevice(id)).thenReturn(responseDTO);

        ResponseEntity<MessageResponse> response = deviceController.configureDevice(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("Device configured successfully");
        assertThat(response.getBody().getData()).isEqualTo(responseDTO);
        verify(deviceConfigurationService, times(1)).configureDevice(id);
    }
}
